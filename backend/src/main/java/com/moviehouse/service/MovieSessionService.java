package com.moviehouse.service;

import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;
import com.moviehouse.dto.SeatStatusDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovieSessionService {
    List<MovieSessionDto> getAllMovieSessions();

    List<MovieSessionDto> getAllMovieSessionsByMovieAndDate(Long movieId, LocalDate date);

    List<MovieSessionDto> getAllMovieSessionsByMovieAndStartTimeAfter(Long movieId, LocalDateTime startTime);

    MovieSessionDto getMovieSessionById(Long id);

    List<SeatStatusDto> getMovieSessionOccupancy(Long sessionId);

    List<MovieSessionDto> createMovieSession(MovieSessionRegistrationDto movieSessionDto);

    MovieSessionDto updateMovieSession(Long id, MovieSessionDto movieSessionDto);

    MovieSessionDto deleteMovieSession(Long id);
}
