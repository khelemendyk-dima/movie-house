package com.moviehouse.service;

import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;
import com.moviehouse.dto.SeatStatusDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovieSessionService {
    List<MovieSessionDto> getAll();

    List<MovieSessionDto> getSessionsByMovieAndDate(Long movieId, LocalDate date);

    List<MovieSessionDto> getSessionsByMovieAndStartTimeAfter(Long movieId, LocalDateTime date);

    MovieSessionDto getById(Long id);

    List<SeatStatusDto> getSessionOccupancy(Long sessionId);

    List<MovieSessionDto> create(MovieSessionRegistrationDto movieSessionDto);

    MovieSessionDto update(Long id, MovieSessionDto movieSessionDto);

    MovieSessionDto delete(Long id);
}
