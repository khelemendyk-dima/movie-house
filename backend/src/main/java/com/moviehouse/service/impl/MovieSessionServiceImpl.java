package com.moviehouse.service.impl;

import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;
import com.moviehouse.dto.SeatStatusDto;
import com.moviehouse.exception.HallNotFoundException;
import com.moviehouse.exception.MovieNotFoundException;
import com.moviehouse.exception.MovieSessionNotFoundException;
import com.moviehouse.model.Hall;
import com.moviehouse.model.Movie;
import com.moviehouse.model.MovieSession;
import com.moviehouse.repository.HallRepository;
import com.moviehouse.repository.MovieRepository;
import com.moviehouse.repository.MovieSessionRepository;
import com.moviehouse.repository.SeatRepository;
import com.moviehouse.service.MovieSessionService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieSessionServiceImpl implements MovieSessionService {
    private final MovieSessionRepository movieSessionRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final ConvertorUtil convertor;

    @Override
    public List<MovieSessionDto> getAllMovieSessions() {
        log.info("Fetching all movie sessions");

        List<MovieSessionDto> sessions = movieSessionRepository.findAll().stream()
                .map(convertor::toMovieSessionDto)
                .toList();

        log.debug("Fetched {} movie sessions", sessions.size());

        return sessions;
    }

    @Override
    public List<MovieSessionDto> getAllMovieSessionsByMovieAndDate(Long movieId, LocalDate date) {
        log.info("Fetching movie sessions for movieId: {} on date: {}", movieId, date);

        List<MovieSessionDto> sessions = movieSessionRepository.findAllByMovieIdAndDate(movieId, date).stream()
                .map(convertor::toMovieSessionDto)
                .toList();

        log.debug("Fetched {} movie sessions for movieId: {} on date: {}", sessions.size(), movieId, date);

        return sessions;
    }

    @Override
    public List<MovieSessionDto> getAllMovieSessionsByMovieAndStartTimeAfter(Long movieId, LocalDateTime startTime) {
        log.info("Fetching movie sessions for movieId: {} after start time: {}", movieId, startTime);

        List<MovieSessionDto> sessions = movieSessionRepository.findAllByMovieIdAndStartTimeAfter(movieId, startTime).stream()
                .map(convertor::toMovieSessionDto)
                .toList();

        log.debug("Fetched {} movie sessions for movieId: {} after start time: {}", sessions.size(), movieId, startTime);

        return sessions;
    }

    @Override
    public MovieSessionDto getMovieSessionById(Long id) {
        log.info("Fetching movie session with id={}", id);

        MovieSessionDto movieSessionDto = convertor.toMovieSessionDto(findMovieSessionById(id));

        log.debug("Fetched movie session: {}", movieSessionDto);

        return movieSessionDto;
    }

    @Override
    public List<SeatStatusDto> getMovieSessionOccupancy(Long sessionId) {
        log.info("Fetching occupancy for movie session with id={}", sessionId);

        MovieSession movieSession = findMovieSessionById(sessionId);
        List<SeatStatusDto> seatStatuses = seatRepository.getSeatStatusesBySessionId(movieSession.getId());

        log.debug("Fetched {} seat statuses for movie session with id={}", seatStatuses.size(), sessionId);

        return seatStatuses;
    }

    @Transactional
    @Override
    public List<MovieSessionDto> createMovieSession(MovieSessionRegistrationDto movieSessionDto) {
        log.info("Creating movie sessions for movieId: {} in hallId: {} starting from date: {} to date: {}",
                movieSessionDto.getMovieId(), movieSessionDto.getHallId(), movieSessionDto.getStartDate(), movieSessionDto.getEndDate());

        Movie movie = findMovieById(movieSessionDto.getMovieId());
        Hall hall = findHallById(movieSessionDto.getHallId());
        List<MovieSession> createdSessions = generateMovieSessions(movieSessionDto, movie, hall);

        List<MovieSessionDto> sessionDtos = movieSessionRepository.saveAll(createdSessions)
                .stream()
                .map(convertor::toMovieSessionDto)
                .toList();

        log.debug("Created {} movie sessions", sessionDtos.size());

        return sessionDtos;
    }

    @Transactional
    @Override
    public MovieSessionDto updateMovieSession(Long id, MovieSessionDto movieSessionDto) {
        log.info("Updating movie session with id={}", id);

        MovieSession existingShowtime = findMovieSessionById(id);
        existingShowtime.setMovie(findMovieById(movieSessionDto.getMovieId()));
        existingShowtime.setHall(findHallById(movieSessionDto.getHallId()));
        existingShowtime.setStartTime(movieSessionDto.getStartTime());
        existingShowtime.setPrice(movieSessionDto.getPrice());

        MovieSession updatedSession = movieSessionRepository.save(existingShowtime);

        log.debug("Updated movie session: {}", updatedSession);

        return convertor.toMovieSessionDto(updatedSession);
    }

    @Transactional
    @Override
    public MovieSessionDto deleteMovieSession(Long id) {
        log.info("Deleting movie session with id={}", id);

        MovieSession movieSession = findMovieSessionById(id);
        movieSessionRepository.delete(movieSession);

        log.debug("Deleted movie session: {}", movieSession);

        return convertor.toMovieSessionDto(movieSession);
    }

    private List<MovieSession> generateMovieSessions(MovieSessionRegistrationDto movieSessionDto, Movie movie, Hall hall) {
        log.info("Generating movie sessions for movie: {} in hall: {} starting from {} to {}",
                movie.getTitle(), hall.getName(), movieSessionDto.getStartDate(), movieSessionDto.getEndDate());

        LocalDate currentDate = movieSessionDto.getStartDate();
        LocalDate endDate = movieSessionDto.getEndDate();
        List<MovieSession> sessions = new ArrayList<>();

        while (!currentDate.isAfter(endDate)) {
            MovieSession session = new MovieSession();
            session.setMovie(movie);
            session.setHall(hall);
            session.setStartTime(currentDate.atTime(movieSessionDto.getStartTime()));
            session.setPrice(movieSessionDto.getPrice());
            sessions.add(session);

            currentDate = currentDate.plusDays(1);
        }

        log.debug("Generated {} movie sessions", sessions.size());

        return sessions;
    }

    private MovieSession findMovieSessionById(Long id) {
        log.info("Searching movie session with id={}", id);

        return movieSessionRepository.findById(id)
                .orElseThrow(() -> new MovieSessionNotFoundException(id));
    }

    private Movie findMovieById(Long id) {
        log.info("Searching movie with id={}", id);

        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    private Hall findHallById(Long id) {
        log.info("Searching hall with id={}", id);

        return hallRepository.findById(id)
                .orElseThrow(() -> new HallNotFoundException(id));
    }
}
