package com.moviehouse.controller;

import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;
import com.moviehouse.dto.SeatStatusDto;
import com.moviehouse.service.MovieSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class MovieSessionController {
    private final MovieSessionService movieSessionService;

    @GetMapping
    public ResponseEntity<List<MovieSessionDto>> getAllSessions(@RequestParam(required = false) Long movieId,
                                                                @RequestParam(required = false) LocalDate date) {
        if (movieId != null && date != null) {
            return ResponseEntity.ok(movieSessionService.getSessionsByMovieAndDate(movieId, date));
        }

        return ResponseEntity.ok(movieSessionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieSessionDto> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(movieSessionService.getById(id));
    }

    @GetMapping("/{id}/occupancy")
    public ResponseEntity<List<SeatStatusDto>> getSessionOccupancy(@PathVariable Long id) {
        return ResponseEntity.ok(movieSessionService.getSessionOccupancy(id));
    }

    @PostMapping
    public ResponseEntity<List<MovieSessionDto>> createSession(@RequestBody @Valid MovieSessionRegistrationDto dto) {
        return ResponseEntity.ok(movieSessionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieSessionDto> updateSession(@PathVariable Long id, @RequestBody @Valid MovieSessionDto dto) {
        return ResponseEntity.ok(movieSessionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieSessionDto> deleteSession(@PathVariable Long id) {
        return ResponseEntity.ok(movieSessionService.delete(id));
    }
}
