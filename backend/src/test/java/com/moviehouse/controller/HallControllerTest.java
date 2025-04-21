package com.moviehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.HallDto;
import com.moviehouse.exception.HallNotFoundException;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.HallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HallController.class)
@AutoConfigureMockMvc(addFilters = false)
class HallControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HallService hallService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private HallDto hallDto;

    @BeforeEach
    void setUp() {
        hallDto = TestDataFactory.createHallDto();
    }

    @Test
    void getAllHalls_shouldReturnListOfHalls() throws Exception {
        List<HallDto> hallDtos = List.of(hallDto, hallDto);

        Mockito.when(hallService.getAllHalls()).thenReturn(hallDtos);

        mockMvc.perform(get("/api/halls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Main Hall"))
                .andExpect(jsonPath("$[1].name").value("Main Hall"));
    }

    @Test
    void getHallById_shouldReturnHallDto() throws Exception {
        Mockito.when(hallService.getHallById(1L)).thenReturn(hallDto);

        mockMvc.perform(get("/api/halls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Hall"))
                .andExpect(jsonPath("$.rowCount").value(1))
                .andExpect(jsonPath("$.seatsPerRow").value(3));
    }

    @Test
    void getHallById_shouldReturn404_whenIdIsInvalid() throws Exception {
        Mockito.when(hallService.getHallById(99L)).thenThrow(HallNotFoundException.class);

        mockMvc.perform(get("/api/halls/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createHall_shouldReturnCreatedHall() throws Exception {
        Mockito.when(hallService.createHall(any())).thenReturn(hallDto);

        mockMvc.perform(post("/api/halls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hallDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Hall"));
    }

    @Test
    void createHall_shouldReturn400_whenInvalidData() throws Exception {
        HallDto invalidDto = new HallDto();

        mockMvc.perform(post("/api/halls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHall_shouldReturnUpdatedHall() throws Exception {
        Mockito.when(hallService.updateHall(eq(1L), any())).thenReturn(hallDto);

        mockMvc.perform(put("/api/halls/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hallDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Hall"));
    }

    @Test
    void updateHall_shouldReturn400_whenInvalidData() throws Exception {
        HallDto invalidDto = new HallDto();

        mockMvc.perform(put("/api/halls/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHall_shouldReturn404_whenIdIsInvalid() throws Exception {
        Mockito.when(hallService.updateHall(eq(99L), any()))
                .thenThrow(HallNotFoundException.class);

        mockMvc.perform(put("/api/halls/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hallDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteHall_shouldReturnDeletedHall() throws Exception {
        Mockito.when(hallService.deleteHall(1L)).thenReturn(hallDto);

        mockMvc.perform(delete("/api/halls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Hall"));
    }

    @Test
    void deleteHall_shouldReturn404_whenIdIsInvalid() throws Exception {
        Mockito.when(hallService.deleteHall(99L)).thenThrow(HallNotFoundException.class);

        mockMvc.perform(delete("/api/halls/99"))
                .andExpect(status().isNotFound());
    }
}
