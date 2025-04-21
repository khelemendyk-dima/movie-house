package com.moviehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;
import com.moviehouse.dto.TicketDto;
import com.moviehouse.exception.MovieSessionNotFoundException;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.MovieSessionService;
import com.moviehouse.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieSessionController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieSessionService movieSessionService;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private MovieSessionDto movieSessionDto;

    @BeforeEach
    void setUp() {
        movieSessionDto = TestDataFactory.createMovieSessionDto();
    }

    @Test
    void getAllSessions_shouldReturnList() throws Exception {
        Mockito.when(movieSessionService.getAllMovieSessions())
                .thenReturn(List.of(movieSessionDto, movieSessionDto));

        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(1L))
                .andExpect(jsonPath("$[1].movieId").value(1L));
    }

    @Test
    void getAllSessions_shouldReturnFilteredList_whenMovieIdSpecified() throws Exception {
        Mockito.when(movieSessionService.getAllMovieSessionsByMovieAndStartTimeAfter(eq(1L), any()))
                .thenReturn(List.of(movieSessionDto, movieSessionDto));

        mockMvc.perform(get("/api/sessions?movieId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(1L))
                .andExpect(jsonPath("$[1].movieId").value(1L));
    }

    @Test
    void getAllSessions_shouldReturnFilteredList_whenMovieIdAndDateSpecified() throws Exception {
        Mockito.when(movieSessionService.getAllMovieSessionsByMovieAndDate(1L, LocalDate.of(2024, 5, 1)))
                .thenReturn(List.of(movieSessionDto, movieSessionDto));

        mockMvc.perform(get("/api/sessions?movieId=1&date=2024-05-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(1L))
                .andExpect(jsonPath("$[1].movieId").value(1L));
    }

    @Test
    void getSessionById_shouldReturnDto() throws Exception {
        Mockito.when(movieSessionService.getMovieSessionById(1L))
                .thenReturn(TestDataFactory.createMovieSessionDto());

        mockMvc.perform(get("/api/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(1L));
    }

    @Test
    void getSessionById_shouldReturn404() throws Exception {
        Mockito.when(movieSessionService.getMovieSessionById(999L))
                .thenThrow(MovieSessionNotFoundException.class);

        mockMvc.perform(get("/api/sessions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSessionOccupancy_shouldReturnList() throws Exception {
        Mockito.when(movieSessionService.getMovieSessionOccupancy(1L))
                .thenReturn(TestDataFactory.createSeatStatusDtos());

        mockMvc.perform(get("/api/sessions/1/occupancy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value("1"))
                .andExpect(jsonPath("$[0].status").value("FREE"))
                .andExpect(jsonPath("$[1].seatNumber").value("2"))
                .andExpect(jsonPath("$[1].status").value("RESERVED"));
    }

    @Test
    void getSessionOccupancy_shouldReturn404() throws Exception {
        Mockito.when(movieSessionService.getMovieSessionOccupancy(999L))
                .thenThrow(MovieSessionNotFoundException.class);

        mockMvc.perform(get("/api/sessions/999/occupancy"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPaidTickets_shouldReturnList() throws Exception {
        TicketDto first = TestDataFactory.createTicketDto();
        first.setSeatNumber(1);
        TicketDto second = TestDataFactory.createTicketDto();
        second.setSeatNumber(2);

        Mockito.when(ticketService.getPaidTicketsBySessionId(1L))
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/sessions/1/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value("1"))
                .andExpect(jsonPath("$[1].seatNumber").value("2"));
    }

    @Test
    void getPaidTickets_shouldReturn404() throws Exception {
        Mockito.when(ticketService.getPaidTicketsBySessionId(999L))
                .thenThrow(MovieSessionNotFoundException.class);

        mockMvc.perform(get("/api/sessions/999/tickets"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createSession_shouldReturnCreatedList() throws Exception {
        MovieSessionRegistrationDto dto = TestDataFactory.createMovieSessionRegistrationDto();
        Mockito.when(movieSessionService.createMovieSession(any()))
                .thenReturn(List.of(movieSessionDto, movieSessionDto));

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(1L))
                .andExpect(jsonPath("$[1].movieId").value(1L));
    }

    @Test
    void createSession_shouldReturn400_whenInvalidRegistrationData() throws Exception {
        MovieSessionRegistrationDto dto = new MovieSessionRegistrationDto();

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSession_shouldReturnUpdatedDto() throws Exception {
        MovieSessionDto dto = TestDataFactory.createMovieSessionDto();
        Mockito.when(movieSessionService.updateMovieSession(eq(1L), any()))
                .thenReturn(dto);

        mockMvc.perform(put("/api/sessions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(1L));
    }

    @Test
    void updateSession_shouldReturn400_whenInvalidMovieSession() throws Exception {
        MovieSessionDto dto = new MovieSessionDto();

        mockMvc.perform(put("/api/sessions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSession_shouldReturn404() throws Exception {
        MovieSessionDto dto = TestDataFactory.createMovieSessionDto();
        Mockito.when(movieSessionService.updateMovieSession(eq(999L), any()))
                .thenThrow(MovieSessionNotFoundException.class);

        mockMvc.perform(put("/api/sessions/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSession_shouldReturnDeletedDto() throws Exception {
        MovieSessionDto dto = TestDataFactory.createMovieSessionDto();
        Mockito.when(movieSessionService.deleteMovieSession(1L)).thenReturn(dto);

        mockMvc.perform(delete("/api/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(1L));
    }

    @Test
    void deleteSession_shouldReturn404() throws Exception {
        Mockito.when(movieSessionService.deleteMovieSession(999L)).thenThrow(MovieSessionNotFoundException.class);

        mockMvc.perform(delete("/api/sessions/999"))
                .andExpect(status().isNotFound());
    }
}
