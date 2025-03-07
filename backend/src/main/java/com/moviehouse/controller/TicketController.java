package com.moviehouse.controller;

import com.moviehouse.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/validate/{ticketId}")
    public ResponseEntity<String> validateTicket(@PathVariable Long ticketId) {
        ticketService.validateTicket(ticketId);

        return ResponseEntity.ok("Ticket is valid!");
    }
}