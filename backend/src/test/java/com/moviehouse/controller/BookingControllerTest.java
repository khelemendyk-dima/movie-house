package com.moviehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.BookingDto;
import com.moviehouse.exception.BookingNotFoundException;
import com.moviehouse.exception.BookingNotPaidException;
import com.moviehouse.exception.MovieSessionNotFoundException;
import com.moviehouse.model.BookingStatus;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.BookingService;
import com.moviehouse.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBooking_shouldReturnBookingDto() throws Exception {
        BookingDto bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING);

        Mockito.when(bookingService.createBooking(any())).thenReturn(bookingDto);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.sessionId").value(1L))
                .andExpect(jsonPath("$.seatIds[0]").value(1L))
                .andExpect(jsonPath("$.seatIds[1]").value(2L));
    }

    @Test
    void createBooking_shouldReturn400_whenInvalidInput() throws Exception {
        BookingDto invalidDto = new BookingDto();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_shouldReturn404_whenBookingNotFound() throws Exception {
        BookingDto bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING);
        bookingDto.setSessionId(999L);

        Mockito.when(bookingService.createBooking(any())).thenThrow(MovieSessionNotFoundException.class);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadTickets_shouldReturnTickets() throws Exception {
        byte[] ticketPdf = new byte[] {1, 2, 3};

        Mockito.when(ticketService.getTicketsPdf(1L)).thenReturn(ticketPdf);

        mockMvc.perform(get("/api/bookings/1/tickets/download"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=tickets.pdf"))
                .andExpect(content().bytes(ticketPdf));
    }

    @Test
    void downloadTickets_shouldReturn402_whenNotPaid() throws Exception {
        Mockito.when(ticketService.getTicketsPdf(1L)).thenThrow(BookingNotPaidException.class);

        mockMvc.perform(get("/api/bookings/1/tickets/download"))
                .andExpect(status().isPaymentRequired());
    }

    @Test
    void downloadTickets_shouldReturn404_whenBookingNotFound() throws Exception {
        Mockito.when(ticketService.getTicketsPdf(999L)).thenThrow(BookingNotFoundException.class);

        mockMvc.perform(get("/api/bookings/999/tickets/download"))
                .andExpect(status().isNotFound());
    }
}
