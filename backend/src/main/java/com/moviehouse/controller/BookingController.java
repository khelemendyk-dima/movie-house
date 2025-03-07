package com.moviehouse.controller;

import com.moviehouse.dto.BookingDto;
import com.moviehouse.service.BookingService;
import com.moviehouse.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid BookingDto dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @GetMapping("/{bookingId}/tickets/download")
    public ResponseEntity<byte[]> downloadTickets(@PathVariable Long bookingId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tickets.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(ticketService.getTicketsPdf(bookingId));
    }
}
