package com.moviehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.MovieDto;
import com.moviehouse.exception.MovieNotFoundException;
import com.moviehouse.exception.PosterNotFoundException;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private MovieDto movieDto;

    @BeforeEach
    void setUp() {
        movieDto = TestDataFactory.createMovieDto();
    }

    @Test
    void getAllMovies_shouldReturnList() throws Exception {
        List<MovieDto> movies = List.of(movieDto, movieDto);
        Mockito.when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Epic Movie"))
                .andExpect(jsonPath("$[1].title").value("Epic Movie"));
    }

    @Test
    void getAllMoviesByDate_shouldReturnList() throws Exception {
        List<MovieDto> movies = List.of(movieDto, movieDto);
        Mockito.when(movieService.getAllUniqueMoviesByStartDate(LocalDate.of(2024, 5, 1))).thenReturn(movies);

        mockMvc.perform(get("/api/movies?date=2024-05-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Epic Movie"))
                .andExpect(jsonPath("$[1].title").value("Epic Movie"));
    }

    @Test
    void getMovieById_shouldReturnMovieDto() throws Exception {
        Mockito.when(movieService.getMovieById(1L)).thenReturn(movieDto);

        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Epic Movie"));
    }

    @Test
    void getMovieById_shouldReturn404() throws Exception {
        Mockito.when(movieService.getMovieById(999L)).thenThrow(MovieNotFoundException.class);

        mockMvc.perform(get("/api/movies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMovie_shouldReturnCreatedMovieDto() throws Exception {
        Mockito.when(movieService.createMovie(any())).thenReturn(movieDto);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Epic Movie"));
    }

    @Test
    void createMovie_shouldReturn400_whenInvalidMovie() throws Exception {
        MovieDto invalidMovie = new MovieDto();

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMovie)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMovie_shouldReturnUpdatedMovieDto() throws Exception {
        Mockito.when(movieService.updateMovie(eq(1L), any())).thenReturn(movieDto);

        mockMvc.perform(put("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Epic Movie"));
    }

    @Test
    void updateMovie_shouldReturn400_whenInvalidMovie() throws Exception {
        MovieDto invalidMovie = new MovieDto();

        mockMvc.perform(put("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMovie)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateMovie_shouldReturn404() throws Exception {
        Mockito.when(movieService.updateMovie(eq(999L), any())).thenThrow(MovieNotFoundException.class);

        mockMvc.perform(put("/api/movies/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMovie_shouldReturnDeletedMovieDto() throws Exception {
        Mockito.when(movieService.deleteMovie(1L)).thenReturn(movieDto);

        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Epic Movie"));
    }

    @Test
    void deleteMovie_shouldReturn404() throws Exception {
        Mockito.when(movieService.deleteMovie(999L)).thenThrow(MovieNotFoundException.class);

        mockMvc.perform(delete("/api/movies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadPoster_shouldReturnPosterUrl() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "poster.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
        Mockito.when(movieService.uploadPoster(any())).thenReturn("poster.jpg");

        mockMvc.perform(multipart("/api/movies/poster/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("poster.jpg")));
    }

    @Test
    void getPoster_shouldReturnImageResource() throws Exception {
        Resource resource = new ByteArrayResource("fake-image-content".getBytes());
        Mockito.when(movieService.getPoster("poster.jpg")).thenReturn(resource);

        mockMvc.perform(get("/api/movies/poster/poster.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }

    @Test
    void getPoster_shouldReturn400_whenImageNotFound() throws Exception {
        Mockito.when(movieService.getPoster("not-found.jpg")).thenThrow(PosterNotFoundException.class);

        mockMvc.perform(get("/api/movies/poster/not-found.jpg"))
                .andExpect(status().isNotFound());
    }
}
