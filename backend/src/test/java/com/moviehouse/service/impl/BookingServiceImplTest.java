package com.moviehouse.service.impl;

import com.moviehouse.dto.BookingDto;
import com.moviehouse.exception.InvalidSeatForHallException;
import com.moviehouse.exception.MovieSessionNotFoundException;
import com.moviehouse.exception.SeatAlreadyBookedException;
import com.moviehouse.exception.SeatNotFoundException;
import com.moviehouse.model.*;
import com.moviehouse.repository.BookingRepository;
import com.moviehouse.repository.MovieSessionRepository;
import com.moviehouse.repository.SeatRepository;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.config.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private MovieSessionRepository movieSessionRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ConvertorUtil convertor;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingDto bookingDto;
    private MovieSession movieSession;
    private List<Seat> bookedSeats;

    @BeforeEach
    void setUp() {
        movieSession = TestDataFactory.createMovieSession();
        bookedSeats = TestDataFactory.createSeats(movieSession.getHall());
        bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING, bookedSeats);
    }

    @Test
    void createBooking_shouldCreateBooking_whenDataIsValid() {
        when(movieSessionRepository.findById(bookingDto.getSessionId())).thenReturn(Optional.of(movieSession));
        when(seatRepository.findAllById(bookingDto.getSeatIds())).thenReturn(bookedSeats);
        when(seatRepository.areSeatsTaken(movieSession.getId(), bookingDto.getSeatIds())).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });
        when(convertor.toBookingDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingService.createBooking(bookingDto);

        assertEquals(bookingDto, result);

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenSessionNotFound() {
        when(movieSessionRepository.findById(bookingDto.getSessionId())).thenReturn(Optional.empty());

        assertThrows(MovieSessionNotFoundException.class, () -> bookingService.createBooking(bookingDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_shouldThrowException_whenSomeSeatsNotFound() {
        List<Seat> partialSeats = List.of(new Seat());

        when(movieSessionRepository.findById(bookingDto.getSessionId())).thenReturn(Optional.of(movieSession));
        when(seatRepository.findAllById(bookingDto.getSeatIds())).thenReturn(partialSeats);

        assertThrows(SeatNotFoundException.class, () -> bookingService.createBooking(bookingDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_shouldThrowException_whenSeatIsFromAnotherHall() {
        Hall anotherHall = TestDataFactory.createHall();
        anotherHall.setId(999L);

        List<Seat> seatsWithInvalidHall = TestDataFactory.createSeats(anotherHall);

        when(movieSessionRepository.findById(bookingDto.getSessionId())).thenReturn(Optional.of(movieSession));
        when(seatRepository.findAllById(bookingDto.getSeatIds())).thenReturn(seatsWithInvalidHall);

        assertThrows(InvalidSeatForHallException.class, () -> bookingService.createBooking(bookingDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_shouldThrowException_whenSeatsAlreadyBooked() {
        when(movieSessionRepository.findById(bookingDto.getSessionId())).thenReturn(Optional.of(movieSession));
        when(seatRepository.findAllById(bookingDto.getSeatIds())).thenReturn(bookedSeats);
        when(seatRepository.areSeatsTaken(movieSession.getId(), bookingDto.getSeatIds())).thenReturn(true);

        assertThrows(SeatAlreadyBookedException.class, () -> bookingService.createBooking(bookingDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void isBookingPaid_shouldReturnTrue_whenBookingIsPaid() {
        Long bookingId = 1L;

        when(bookingRepository.existsByIdAndStatus(bookingId, BookingStatus.PAID)).thenReturn(true);

        boolean result = bookingService.isBookingPaid(bookingId);

        assertTrue(result);
    }

    @Test
    void isBookingPaid_shouldReturnFalse_whenBookingIsNotPaid() {
        Long bookingId = 1L;

        when(bookingRepository.existsByIdAndStatus(bookingId, BookingStatus.PAID)).thenReturn(false);

        boolean result = bookingService.isBookingPaid(bookingId);

        assertFalse(result);
    }

    @Test
    void removeExpiredBookings_shouldDeleteExpiredBookings() {
        when(bookingRepository.deleteExpiredBookings(any(LocalDateTime.class), eq(BookingStatus.PENDING)))
                .thenReturn(3);

        bookingService.removeExpiredBookings();

        verify(bookingRepository).deleteExpiredBookings(any(LocalDateTime.class), eq(BookingStatus.PENDING));
    }

}
