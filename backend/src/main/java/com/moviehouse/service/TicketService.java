package com.moviehouse.service;

import com.moviehouse.dto.TicketDto;
import com.moviehouse.model.Ticket;

import java.util.List;

public interface TicketService {
    List<TicketDto> getPaidTicketsBySessionId(Long sessionId);

    byte[] generateTicketsPdf(List<Ticket> tickets);

    void saveTicketsPdf(byte[] pdf, Long bookingId);

    byte[] getTicketsPdf(Long bookingId);

    void validateTicket(Long ticketId);
}
