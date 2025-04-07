package com.moviehouse.controller;

import com.moviehouse.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "Endpoints for managing tickets")
public class TicketController {
    private final TicketService ticketService;

    @Operation(summary = "Validate a ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket is valid!"),
            @ApiResponse(responseCode = "402", description = "Booking wasn't paid."),
            @ApiResponse(responseCode = "404", description = "Ticket not found."),
            @ApiResponse(responseCode = "409", description = "Ticket is already used.")
    })
    @GetMapping("/validate/{ticketId}")
    public ResponseEntity<String> validateTicket(@PathVariable Long ticketId) {
        log.info("Received request to validate ticket by id={}", ticketId);

        ticketService.validateTicket(ticketId);

        return ResponseEntity.ok("Ticket is valid!");
    }
}