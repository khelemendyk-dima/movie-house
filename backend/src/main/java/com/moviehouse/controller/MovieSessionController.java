package com.moviehouse.controller;

import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;
import com.moviehouse.dto.SeatStatusDto;
import com.moviehouse.dto.TicketDto;
import com.moviehouse.service.MovieSessionService;
import com.moviehouse.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Tag(name = "Movie Sessions", description = "Endpoints for managing movie sessions")
public class MovieSessionController {
    private final MovieSessionService movieSessionService;
    private final TicketService ticketService;

    @Operation(summary = "Get all sessions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of movie sessions.")
    })
    @GetMapping
    public ResponseEntity<List<MovieSessionDto>> getAllSessions(@RequestParam(required = false) Long movieId,
                                                                @RequestParam(required = false) LocalDate date) {
        if (movieId != null && date != null) {
            log.info("Received request to get all sessions by movieId={} and date={}", movieId, date);
            return ResponseEntity.ok(movieSessionService.getAllMovieSessionsByMovieAndDate(movieId, date));
        }

        if (movieId != null) {
            log.info("Received request to get all sessions by movieId={}", movieId);
            return ResponseEntity.ok(movieSessionService.getAllMovieSessionsByMovieAndStartTimeAfter(movieId, LocalDateTime.now()));
        }

        log.info("Received request to get all sessions");
        return ResponseEntity.ok(movieSessionService.getAllMovieSessions());
    }

    @Operation(summary = "Get movie session by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movie session details."),
            @ApiResponse(responseCode = "404", description = "Movie session not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovieSessionDto> getSessionById(@PathVariable Long id) {
        log.info("Received request to get movie session by id={}", id);

        return ResponseEntity.ok(movieSessionService.getMovieSessionById(id));
    }

    @Operation(summary = "Get session occupancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved session occupancy."),
            @ApiResponse(responseCode = "404", description = "Session not found.")
    })
    @GetMapping("/{id}/occupancy")
    public ResponseEntity<List<SeatStatusDto>> getSessionOccupancy(@PathVariable Long id) {
        log.info("Received request to get session occupancy by id={}", id);

        return ResponseEntity.ok(movieSessionService.getMovieSessionOccupancy(id));
    }

    @Operation(summary = "Get paid tickets for a session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paid tickets."),
            @ApiResponse(responseCode = "404", description = "Session not found.")
    })
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketDto>> getPaidTickets(@PathVariable Long id) {
        log.info("Received request to get paid tickets for a session with id={}", id);

        return ResponseEntity.ok(ticketService.getPaidTicketsBySessionId(id));
    }

    @Operation(summary = "Create a new movie session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new movie session."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data.")
    })
    @PostMapping
    public ResponseEntity<List<MovieSessionDto>> createSession(@RequestBody @Valid MovieSessionRegistrationDto dto) {
        log.info("Received request to create a new movie session for movieId={} and hallId={}", dto.getMovieId(), dto.getHallId());

        return ResponseEntity.ok(movieSessionService.createMovieSession(dto));
    }

    @Operation(summary = "Update a movie session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the movie session."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "404", description = "Movie session not found.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MovieSessionDto> updateSession(@PathVariable Long id, @RequestBody @Valid MovieSessionDto dto) {
        log.info("Received request to update session by id={}", id);

        return ResponseEntity.ok(movieSessionService.updateMovieSession(id, dto));
    }

    @Operation(summary = "Delete a movie session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the movie session."),
            @ApiResponse(responseCode = "404", description = "Movie session not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MovieSessionDto> deleteSession(@PathVariable Long id) {
        log.info("Received request to delete session by id={}", id);

        return ResponseEntity.ok(movieSessionService.deleteMovieSession(id));
    }
}
