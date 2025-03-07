package com.moviehouse.service.impl;

import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;
import com.moviehouse.dto.SeatStatusDto;
import com.moviehouse.exceptions.HallNotFoundException;
import com.moviehouse.exceptions.MovieNotFoundException;
import com.moviehouse.exceptions.MovieSessionNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public List<MovieSessionDto> getAll() {
        return movieSessionRepository.findAll().stream()
                .map(convertor::toMovieSessionDto)
                .toList();
    }

    @Override
    public MovieSessionDto getById(Long id) {
        return convertor.toMovieSessionDto(findMovieSessionById(id));
    }

    @Override
    public List<SeatStatusDto> getSessionOccupancy(Long sessionId) {
        // throw exception if not found
        findMovieSessionById(sessionId);

        return seatRepository.getSeatStatusesBySessionId(sessionId);
    }

    @Transactional
    @Override
    public List<MovieSessionDto> create(MovieSessionRegistrationDto movieSessionDto) {
        Movie movie = findMovieById(movieSessionDto.getMovieId());
        Hall hall = findHallById(movieSessionDto.getHallId());

        List<MovieSession> createdSessions = generateMovieSessions(movieSessionDto, movie, hall);

        return movieSessionRepository.saveAll(createdSessions)
                .stream()
                .map(convertor::toMovieSessionDto)
                .toList();
    }

    @Transactional
    @Override
    public MovieSessionDto update(Long id, MovieSessionDto movieSessionDto) {
        MovieSession existingShowtime = findMovieSessionById(id);

        existingShowtime.setMovie(findMovieById(movieSessionDto.getMovieId()));
        existingShowtime.setHall(findHallById(movieSessionDto.getHallId()));
        existingShowtime.setStartTime(movieSessionDto.getStartTime());
        existingShowtime.setPrice(movieSessionDto.getPrice());

        return convertor.toMovieSessionDto(movieSessionRepository.save(existingShowtime));
    }

    @Transactional
    @Override
    public MovieSessionDto delete(Long id) {
        MovieSession movieSession = findMovieSessionById(id);
        movieSessionRepository.delete(movieSession);

        return convertor.toMovieSessionDto(movieSession);
    }

    private List<MovieSession> generateMovieSessions(MovieSessionRegistrationDto movieSessionDto, Movie movie, Hall hall) {
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

        return sessions;
    }

    private MovieSession findMovieSessionById(Long id) {
        return movieSessionRepository.findById(id)
                .orElseThrow(() -> new MovieSessionNotFoundException(id));
    }

    private Movie findMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    private Hall findHallById(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new HallNotFoundException(id));
    }
}
