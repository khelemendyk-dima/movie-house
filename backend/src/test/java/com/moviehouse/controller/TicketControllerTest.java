package com.moviehouse.controller;

import com.moviehouse.exception.BookingNotPaidException;
import com.moviehouse.exception.TicketAlreadyUsedException;
import com.moviehouse.exception.TicketNotFoundException;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;


    @Test
    void validateTicket_shouldReturn200() throws Exception {
        Long ticketId = 1L;

        mockMvc.perform(get("/api/tickets/validate/{ticketId}", ticketId))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket is valid!"));

        Mockito.verify(ticketService).validateTicket(ticketId);
    }

    @Test
    void validateTicket_shouldReturn402_whenBookingNotPaid() throws Exception {
        Long ticketId = 2L;

        Mockito.doThrow(BookingNotPaidException.class)
                .when(ticketService).validateTicket(ticketId);

        mockMvc.perform(get("/api/tickets/validate/{ticketId}", ticketId))
                .andExpect(status().isPaymentRequired());
    }

    @Test
    void validateTicket_shouldReturn404_whenTicketNotFound() throws Exception {
        Long ticketId = 404L;

        Mockito.doThrow(TicketNotFoundException.class)
                .when(ticketService).validateTicket(ticketId);

        mockMvc.perform(get("/api/tickets/validate/{ticketId}", ticketId))
                .andExpect(status().isNotFound());
    }

    @Test
    void validateTicket_shouldReturn409_whenTicketAlreadyUsed() throws Exception {
        Long ticketId = 409L;

        Mockito.doThrow(TicketAlreadyUsedException.class)
                .when(ticketService).validateTicket(ticketId);

        mockMvc.perform(get("/api/tickets/validate/{ticketId}", ticketId))
                .andExpect(status().isConflict());
    }
}
