package com.moviehouse.service;

import com.moviehouse.model.Ticket;

import java.util.List;

public interface TicketService {
    byte[] generateTicketsPdf(List<Ticket> tickets);

    void saveTicketsPdf(byte[] pdf, Long bookingId);

    byte[] getTicketsPdf(Long bookingId);

    void validateTicket(Long ticketId);
}
