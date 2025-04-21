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
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.config.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieSessionServiceImplTest {

    @Mock
    private MovieSessionRepository movieSessionRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private HallRepository hallRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ConvertorUtil convertor;

    @InjectMocks
    private MovieSessionServiceImpl movieSessionService;

    private Long movieId;
    private LocalDate date;
    private Movie movie;
    private Hall hall;
    private LocalDateTime startTime;
    private MovieSession movieSession;
    private MovieSessionDto movieSessionDto;
    private List<MovieSession> movieSessions;
    private List<MovieSessionDto> movieSessionDtos;
    private List<SeatStatusDto> seatStatusDtos;
    private MovieSessionRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        movieId = 1L;
        date = LocalDate.now();
        startTime = LocalDateTime.now();
        movie = TestDataFactory.createMovie();
        hall = TestDataFactory.createHall();
        movieSession = TestDataFactory.createMovieSession();
        movieSessionDto = TestDataFactory.createMovieSessionDto();
        movieSessions = List.of(movieSession, movieSession);
        movieSessionDtos = List.of(movieSessionDto, movieSessionDto);
        seatStatusDtos = TestDataFactory.createSeatStatusDtos();
        registrationDto = TestDataFactory.createMovieSessionRegistrationDto();
    }

    @Test
    void getAllMovieSessions_shouldReturnListOfMovieSessionDtos() {
        when(movieSessionRepository.findAll()).thenReturn(movieSessions);
        when(convertor.toMovieSessionDto(movieSession)).thenReturn(movieSessionDto);

        List<MovieSessionDto> result = movieSessionService.getAllMovieSessions();

        assertEquals(movieSessionDtos.size(), result.size());
        assertArrayEquals(movieSessionDtos.toArray(), result.toArray());

        verify(movieSessionRepository).findAll();
        verify(convertor, times(2)).toMovieSessionDto(any(MovieSession.class));
    }

    @Test
    void getAllMovieSessions_shouldReturnEmptyList_whenNoMovieSessions() {
        when(movieSessionRepository.findAll()).thenReturn(Collections.emptyList());

        List<MovieSessionDto> result = movieSessionService.getAllMovieSessions();

        assertEquals(0, result.size());

        verify(movieSessionRepository).findAll();
        verify(convertor, never()).toMovieSessionDto(any(MovieSession.class));
    }

    @Test
    void getAllMovieSessionsByMovieAndDate_shouldReturnListOfMovieSessionDtos() {
        when(movieSessionRepository.findAllByMovieIdAndDate(movieId, date)).thenReturn(movieSessions);
        when(convertor.toMovieSessionDto(movieSession)).thenReturn(movieSessionDto);

        List<MovieSessionDto> result = movieSessionService.getAllMovieSessionsByMovieAndDate(movieId, date);

        assertEquals(movieSessionDtos.size(), result.size());
        assertArrayEquals(movieSessionDtos.toArray(), result.toArray());

        verify(movieSessionRepository).findAllByMovieIdAndDate(movieId, date);
        verify(convertor, times(2)).toMovieSessionDto(any(MovieSession.class));
    }

    @Test
    void getAllMovieSessionsByMovieAndDate_shouldReturnEmptyList_whenNoMovieSessions() {
        when(movieSessionRepository.findAllByMovieIdAndDate(movieId, date)).thenReturn(Collections.emptyList());

        List<MovieSessionDto> result = movieSessionService.getAllMovieSessionsByMovieAndDate(movieId, date);

        assertEquals(0, result.size());

        verify(movieSessionRepository).findAllByMovieIdAndDate(movieId, date);
        verify(convertor, never()).toMovieSessionDto(any(MovieSession.class));
    }

    @Test
    void getAllMovieSessionsByMovieAndStartTimeAfter_shouldReturnListOfMovieSessionDtos() {
        when(movieSessionRepository.findAllByMovieIdAndStartTimeAfter(movieId, startTime)).thenReturn(movieSessions);
        when(convertor.toMovieSessionDto(movieSession)).thenReturn(movieSessionDto);

        List<MovieSessionDto> result = movieSessionService.getAllMovieSessionsByMovieAndStartTimeAfter(movieId, startTime);

        assertEquals(movieSessionDtos.size(), result.size());
        assertArrayEquals(movieSessionDtos.toArray(), result.toArray());

        verify(movieSessionRepository).findAllByMovieIdAndStartTimeAfter(movieId, startTime);
        verify(convertor, times(2)).toMovieSessionDto(any(MovieSession.class));
    }

    @Test
    void getAllMovieSessionsByMovieAndStartTimeAfter_shouldReturnEmptyList_whenNoMovieSessions() {
        when(movieSessionRepository.findAllByMovieIdAndStartTimeAfter(movieId, startTime)).thenReturn(Collections.emptyList());

        List<MovieSessionDto> result = movieSessionService.getAllMovieSessionsByMovieAndStartTimeAfter(movieId, startTime);

        assertEquals(0, result.size());

        verify(movieSessionRepository).findAllByMovieIdAndStartTimeAfter(movieId, startTime);
        verify(convertor, never()).toMovieSessionDto(any(MovieSession.class));
    }

    @Test
    void getMovieSessionById_shouldReturnMovieSessionDto_whenMovieSessionExists() {
        when(movieSessionRepository.findById(movieSession.getId())).thenReturn(Optional.of(movieSession));
        when(convertor.toMovieSessionDto(movieSession)).thenReturn(movieSessionDto);

        MovieSessionDto result = movieSessionService.getMovieSessionById(movieSession.getId());

        assertEquals(movieSessionDto, result);

        verify(movieSessionRepository).findById(movieSession.getId());
        verify(convertor).toMovieSessionDto(any(MovieSession.class));
    }

    @Test
    void getMovieSessionById_shouldThrowException_whenMovieSessionNotFound() {
        when(movieSessionRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(MovieSessionNotFoundException.class, () -> movieSessionService.getMovieSessionById(404L));
    }

    @Test
    void getMovieSessionOccupancy_shouldReturnListOfSeatStatusDtos() {
        when(movieSessionRepository.findById(movieSession.getId())).thenReturn(Optional.of(movieSession));
        when(seatRepository.getSeatStatusesBySessionId(movieSession.getId())).thenReturn(seatStatusDtos);

        List<SeatStatusDto> result = movieSessionService.getMovieSessionOccupancy(movieSession.getId());

        assertEquals(seatStatusDtos.size(), result.size());
        assertArrayEquals(seatStatusDtos.toArray(), result.toArray());

        verify(movieSessionRepository).findById(movieSession.getId());
        verify(seatRepository).getSeatStatusesBySessionId(movieSession.getId());
    }

    @Test
    void getMovieSessionOccupancy_shouldThrowException_whenMovieSessionNotFound() {
        when(movieSessionRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(MovieSessionNotFoundException.class,
                () -> movieSessionService.getMovieSessionOccupancy(404L));

        verify(movieSessionRepository).findById(404L);
        verify(seatRepository, never()).getSeatStatusesBySessionId(anyLong());
    }

    @Test
    void getMovieSessionOccupancy_shouldReturnEmptyList_whenNoMovieSessionOccupancy() {
        when(movieSessionRepository.findById(movieSession.getId())).thenReturn(Optional.of(movieSession));
        when(seatRepository.getSeatStatusesBySessionId(movieSession.getId())).thenReturn(Collections.emptyList());

        List<SeatStatusDto> result = movieSessionService.getMovieSessionOccupancy(movieSession.getId());

        assertEquals(0, result.size());

        verify(movieSessionRepository).findById(movieSession.getId());
        verify(seatRepository).getSeatStatusesBySessionId(movieSession.getId());
    }

    //    ==========================
    @Test
    void createMovieSession_shouldCreateMovieSessionAndReturnDto() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(movieSessionRepository.saveAll(anyList())).thenReturn(movieSessions);
        when(convertor.toMovieSessionDto(any())).thenReturn(movieSessionDto);

        List<MovieSessionDto> result = movieSessionService.createMovieSession(registrationDto);

        assertEquals(movieSessionDtos.size(), result.size());
        assertArrayEquals(movieSessionDtos.toArray(), result.toArray());

        verify(movieSessionRepository).saveAll(anyList());
    }

    @Test
    void createMovieSession_shouldThrowException_whenMovieNotFound() {
        registrationDto.setMovieId(404L);
        when(movieRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieSessionService.createMovieSession(registrationDto));

        verify(movieSessionRepository, never()).saveAll(anyList());
    }

    @Test
    void createMovieSession_shouldThrowException_whenHallNotFound() {
        registrationDto.setHallId(404L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(HallNotFoundException.class, () -> movieSessionService.createMovieSession(registrationDto));

        verify(movieSessionRepository, never()).saveAll(anyList());
    }

    @Test
    void updateMovieSession_shouldUpdateAndReturnDto_whenMovieSessionExists() {
        MovieSession existing = movieSession;
        MovieSession updated = TestDataFactory.createMovieSession();
        updated.setId(null);

        when(movieSessionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(movieSessionRepository.save(existing)).thenReturn(updated);
        when(convertor.toMovieSessionDto(updated)).thenReturn(movieSessionDto);

        MovieSessionDto result = movieSessionService.updateMovieSession(1L, movieSessionDto);

        assertEquals(movieSessionDto, result);

        verify(movieSessionRepository).save(existing);
    }

    @Test
    void updateMovieSession_shouldThrowException_whenMovieSessionNotFound() {
        when(movieSessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovieSessionNotFoundException.class, () -> movieSessionService.updateMovieSession(1L, movieSessionDto));

        verify(movieSessionRepository, never()).save(any());
    }

    @Test
    void updateMovieSession_shouldThrowException_whenMovieNotFound() {
        when(movieSessionRepository.findById(1L)).thenReturn(Optional.of(movieSession));
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieSessionService.updateMovieSession(1L, movieSessionDto));

        verify(movieSessionRepository, never()).save(any());
    }

    @Test
    void updateMovieSession_shouldThrowException_whenHallNotFound() {
        when(movieSessionRepository.findById(1L)).thenReturn(Optional.of(movieSession));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HallNotFoundException.class, () -> movieSessionService.updateMovieSession(1L, movieSessionDto));

        verify(movieSessionRepository, never()).save(any());
    }

    @Test
    void deleteMovieSession_shouldDeleteAndReturnDto() {
        when(movieSessionRepository.findById(1L)).thenReturn(Optional.of(movieSession));
        when(convertor.toMovieSessionDto(movieSession)).thenReturn(movieSessionDto);

        MovieSessionDto result = movieSessionService.deleteMovieSession(1L);

        assertEquals(movieSessionDto, result);

        verify(movieSessionRepository).delete(movieSession);
    }

    @Test
    void deleteMovieSession_shouldThrowException_whenMovieSessionNotFound() {
        when(movieSessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovieSessionNotFoundException.class, () -> movieSessionService.deleteMovieSession(1L));

        verify(movieSessionRepository, never()).delete(movieSession);
    }
}