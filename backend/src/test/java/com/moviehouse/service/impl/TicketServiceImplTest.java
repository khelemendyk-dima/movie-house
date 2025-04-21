package com.moviehouse.service.impl;

import com.moviehouse.dto.TicketDto;
import com.moviehouse.exception.*;
import com.moviehouse.model.*;
import com.moviehouse.repository.TicketRepository;
import com.moviehouse.service.BookingService;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.config.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private ConvertorUtil convertor;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Ticket ticket;
    private TicketDto ticketDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(ticketService, "baseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(ticketService, "ticketsDir", "test-tickets/");
        ReflectionTestUtils.setField(ticketService, "imgDir", "src/test/resources/images/");

        Movie movie = TestDataFactory.createMovie();
        Hall hall = TestDataFactory.createHall();
        MovieSession session = TestDataFactory.createMovieSession(movie, hall);
        Seat seat = TestDataFactory.createSeat(hall);
        Booking booking = TestDataFactory.createBooking(BookingStatus.PAID, session);

        ticket = TestDataFactory.createTicket(booking, session, seat);
        ticketDto = TestDataFactory.createTicketDto();
    }

    @Test
    void getPaidTicketsBySessionId_shouldReturnListOfTicketDtos() {
        List<Ticket> tickets = List.of(ticket, ticket);
        List<TicketDto> ticketDtos = List.of(ticketDto, ticketDto);

        when(ticketRepository.findPaidTicketsBySessionId(1L)).thenReturn(tickets);
        when(convertor.toTicketDto(ticket)).thenReturn(ticketDto);

        List<TicketDto> result = ticketService.getPaidTicketsBySessionId(1L);

        assertEquals(ticketDtos.size(), result.size());
        assertArrayEquals(ticketDtos.toArray(), result.toArray());

        verify(ticketRepository).findPaidTicketsBySessionId(1L);
        verify(convertor, times(2)).toTicketDto(ticket);
    }

    @Test
    void getPaidTicketsBySessionId_shouldReturnEmptyList_whenNoTicketsForSession() {
        when(ticketRepository.findPaidTicketsBySessionId(1L)).thenReturn(Collections.emptyList());

        List<TicketDto> result = ticketService.getPaidTicketsBySessionId(1L);

        assertEquals(0, result.size());

        verify(ticketRepository).findPaidTicketsBySessionId(1L);
        verify(convertor, never()).toTicketDto(any(Ticket.class));
    }

    @Test
    void getTicketsPdf_shouldReturnPdfBytes_whenFileExistsAndBookingPaid() throws IOException {
        Long bookingId = 1L;
        String filename = "ticket-" + bookingId + ".pdf";
        Path path = Paths.get("test-tickets").resolve(filename);
        Files.createDirectories(path.getParent());
        Files.write(path, new byte[]{1, 2, 3});

        when(bookingService.isBookingPaid(bookingId)).thenReturn(true);

        byte[] result = ticketService.getTicketsPdf(bookingId);

        assertArrayEquals(new byte[]{1, 2, 3}, result);
        verify(bookingService).isBookingPaid(bookingId);

        // cleanup
        Files.deleteIfExists(path);
        Files.deleteIfExists(path.getParent());
    }

    @Test
    void getTicketsPdf_shouldThrowException_whenFileNotExists() {
        when(bookingService.isBookingPaid(2L)).thenReturn(true);

        assertThrows(TicketFileNotFoundException.class, () -> ticketService.getTicketsPdf(2L));
    }

    @Test
    void getTicketsPdf_shouldThrowException_whenBookingNotPaid() {
        when(bookingService.isBookingPaid(3L)).thenReturn(false);

        assertThrows(BookingNotPaidException.class, () -> ticketService.getTicketsPdf(3L));
    }

    @Test
    void generateTicketsPdf_shouldReturnPdfBytes_withContent() {
        List<Ticket> tickets = List.of(ticket, ticket);

        byte[] pdfBytes = ticketService.generateTicketsPdf(tickets);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void generateTicketsPdf_shouldThrowServiceException_whenLogoMissing() {
        ReflectionTestUtils.setField(ticketService, "imgDir", "non/existing/path/");

        List<Ticket> tickets = List.of(ticket, ticket);

        assertThrows(ServiceException.class, () -> ticketService.generateTicketsPdf(tickets));
    }

    @Test
    void saveTicketsPdf_shouldWritePdfFileSuccessfully() throws IOException {
        byte[] pdfBytes = "dummyPdfContent".getBytes();
        Long bookingId = 1L;

        ticketService.saveTicketsPdf(pdfBytes, bookingId);

        Path expectedPath = Paths.get("test-tickets").resolve("ticket-" + bookingId + ".pdf");
        assertTrue(Files.exists(expectedPath));
        assertArrayEquals(pdfBytes, Files.readAllBytes(expectedPath));

        Files.deleteIfExists(expectedPath);
        Files.deleteIfExists(expectedPath.getParent());
    }

    @Test
    void saveTicketsPdf_shouldThrowException_whenDirectoryIsInvalid() {
        ReflectionTestUtils.setField(ticketService, "ticketsDir", "/dev/null/invalid/");

        byte[] dummy = "test".getBytes();
        Long bookingId = 99L;

        assertThrows(TicketSaveException.class, () -> ticketService.saveTicketsPdf(dummy, bookingId));
    }

    @Test
    void validateTicket_shouldMarkTicketAsUsed_whenValid() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(bookingService.isBookingPaid(1L)).thenReturn(true);

        ticketService.validateTicket(1L);

        assertTrue(ticket.isUsed());

        verify(ticketRepository).save(ticket);
    }

    @Test
    void validateTicket_shouldThrowException_whenTicketNotFound() {
        when(ticketRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.validateTicket(404L));

        verify(ticketRepository, never()).save(ticket);
    }

    @Test
    void validateTicket_shouldThrowException_whenBookingNotPaid() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(bookingService.isBookingPaid(1L)).thenReturn(false);

        assertThrows(BookingNotPaidException.class, () -> ticketService.validateTicket(1L));

        verify(ticketRepository, never()).save(ticket);
    }

    @Test
    void validateTicket_shouldThrowException_whenTicketAlreadyUsed() {
        ticket.setUsed(true);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(bookingService.isBookingPaid(1L)).thenReturn(true);

        assertThrows(TicketAlreadyUsedException.class, () -> ticketService.validateTicket(1L));

        verify(ticketRepository, never()).save(ticket);
    }
}
