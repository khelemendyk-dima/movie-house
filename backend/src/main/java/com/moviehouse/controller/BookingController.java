package com.moviehouse.controller;

import com.moviehouse.dto.BookingDto;
import com.moviehouse.service.BookingService;
import com.moviehouse.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Endpoints for managing bookings and download tickets")
public class BookingController {
    private final BookingService bookingService;
    private final TicketService ticketService;

    @Operation(summary = "Create a new booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking created successfully."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "404", description = "Session not found for creating booking.")
    })
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid BookingDto dto) {
        log.info("Received request to create booking with email={}, sessionId={}, seatIds={}", dto.getEmail(), dto.getSessionId(), dto.getSeatIds());

        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @Operation(summary = "Download tickets for a booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets downloaded successfully."),
            @ApiResponse(responseCode = "402", description = "Booking not paid."),
            @ApiResponse(responseCode = "404", description = "Booking not found.")
    })
    @GetMapping("/{bookingId}/tickets/download")
    public ResponseEntity<byte[]> downloadTickets(@PathVariable Long bookingId) {
        log.info("Received request to download tickets by bookingId={}", bookingId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tickets.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(ticketService.getTicketsPdf(bookingId));
    }
}
