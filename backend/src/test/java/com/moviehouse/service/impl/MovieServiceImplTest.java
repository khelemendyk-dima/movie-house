package com.moviehouse.service.impl;

import com.moviehouse.dto.MovieDto;
import com.moviehouse.exception.GenreNotFoundException;
import com.moviehouse.exception.MovieNotFoundException;
import com.moviehouse.exception.PosterNotFoundException;
import com.moviehouse.exception.ServiceException;
import com.moviehouse.model.Movie;
import com.moviehouse.repository.GenreRepository;
import com.moviehouse.repository.MovieRepository;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.config.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private ConvertorUtil convertor;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;
    private MovieDto movieDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(movieService, "postersDir", "test-posters/");

        movie = TestDataFactory.createMovie();
        movieDto = TestDataFactory.createMovieDto();
    }

    @Test
    void getAllMovies_shouldReturnListOfMovieDtos() {
        List<Movie> movies = List.of(movie, movie);
        List<MovieDto> movieDtos = List.of(movieDto, movieDto);

        when(movieRepository.findAll()).thenReturn(movies);
        when(convertor.toMovieDto(movie)).thenReturn(movieDto);

        List<MovieDto> result = movieService.getAllMovies();

        assertEquals(movieDtos.size(), result.size());
        assertArrayEquals(movieDtos.toArray(), result.toArray());

        verify(movieRepository).findAll();
        verify(convertor, times(2)).toMovieDto(any(Movie.class));
    }

    @Test
    void getAllMovies_shouldReturnEmptyList_whenNoMovies() {
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        List<MovieDto> result = movieService.getAllMovies();

        assertEquals(0, result.size());

        verify(movieRepository).findAll();
        verify(convertor, never()).toMovieDto(any(Movie.class));
    }

    @Test
    void getAllUniqueMoviesByStartDate_shouldReturnListOfMovieDtos() {
        List<Movie> movies = List.of(movie, movie);
        List<MovieDto> movieDtos = List.of(movieDto, movieDto);
        LocalDate startDate = LocalDate.now();

        when(movieRepository.findAllUniqueMoviesStartByDate(startDate)).thenReturn(movies);
        when(convertor.toMovieDto(movie)).thenReturn(movieDto);

        List<MovieDto> result = movieService.getAllUniqueMoviesByStartDate(startDate);

        assertEquals(movieDtos.size(), result.size());
        assertArrayEquals(movieDtos.toArray(), result.toArray());

        verify(movieRepository).findAllUniqueMoviesStartByDate(startDate);
        verify(convertor, times(2)).toMovieDto(any(Movie.class));
    }

    @Test
    void getAllUniqueMoviesByStartDate_shouldReturnEmptyList_whenNoMovies() {
        LocalDate startDate = LocalDate.now();

        when(movieRepository.findAllUniqueMoviesStartByDate(startDate)).thenReturn(Collections.emptyList());

        List<MovieDto> result = movieService.getAllUniqueMoviesByStartDate(startDate);

        assertEquals(0, result.size());

        verify(movieRepository).findAllUniqueMoviesStartByDate(startDate);
        verify(convertor, never()).toMovieDto(any(Movie.class));
    }

    @Test
    void getMovieById_shouldReturnDto_whenExists() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(convertor.toMovieDto(movie)).thenReturn(movieDto);

        MovieDto result = movieService.getMovieById(1L);

        assertEquals(movieDto, result);

        verify(movieRepository).findById(1L);
        verify(convertor).toMovieDto(any(Movie.class));
    }

    @Test
    void getMovieById_shouldThrowException_whenNotFound() {
        when(movieRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(404L));
    }

    @Test
    void createMovie_shouldSaveAndReturnDto() {
        when(convertor.toMovie(movieDto)).thenReturn(movie);
        when(genreRepository.findByName("Action")).thenReturn(Optional.of(TestDataFactory.createGenre(1L, "Action")));
        when(genreRepository.findByName("Adventure")).thenReturn(Optional.of(TestDataFactory.createGenre(2L, "Adventure")));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(convertor.toMovieDto(movie)).thenReturn(movieDto);

        MovieDto result = movieService.createMovie(movieDto);

        assertEquals(movieDto, result);

        verify(movieRepository).save(movie);
        verify(genreRepository, times(2)).findByName(anyString());
    }

    @Test
    void createMovie_shouldThrowException_whenGenreMissing() {
        movieDto.setGenres(Set.of("Unknown"));

        when(convertor.toMovie(movieDto)).thenReturn(new Movie());
        when(genreRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> movieService.createMovie(movieDto));

        verify(movieRepository, never()).save(movie);
    }

    @Test
    void updateMovie_shouldUpdateAndReturnDto() {
        Movie existing = movie;
        Movie updated = TestDataFactory.createMovie();
        updated.setId(null);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(convertor.toMovie(movieDto)).thenReturn(updated);
        when(genreRepository.findByName("Action")).thenReturn(Optional.of(TestDataFactory.createGenre(1L, "Action")));
        when(genreRepository.findByName("Adventure")).thenReturn(Optional.of(TestDataFactory.createGenre(2L, "Adventure")));
        when(movieRepository.save(updated)).thenReturn(updated);
        when(convertor.toMovieDto(updated)).thenReturn(movieDto);

        MovieDto result = movieService.updateMovie(1L, movieDto);

        assertEquals(movieDto, result);

        verify(movieRepository).findById(1L);
        verify(genreRepository, times(2)).findByName(anyString());
        verify(movieRepository).save(updated);
    }

    @Test
    void updateMovie_shouldThrowException_whenMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(1L, movieDto));

        verify(movieRepository, never()).save(movie);
    }

    @Test
    void updateMovie_shouldThrowException_whenGenreMissing() {
        movieDto.setGenres(Set.of("Unknown"));

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(convertor.toMovie(movieDto)).thenReturn(new Movie());
        when(genreRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> movieService.updateMovie(1L, movieDto));

        verify(movieRepository, never()).save(movie);
    }

    @Test
    void deleteMovie_shouldRemoveAndReturnDto() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(convertor.toMovieDto(movie)).thenReturn(movieDto);

        MovieDto result = movieService.deleteMovie(1L);

        assertEquals(movieDto, result);

        verify(movieRepository).findById(1L);
        verify(movieRepository).delete(movie);
    }

    @Test
    void deleteMovie_shouldThrowException_whenMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(1L));

        verify(movieRepository).findById(1L);
        verify(movieRepository, never()).delete(movie);
    }

    @Test
    void uploadPoster_shouldSaveFileAndReturnFilename() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "poster.jpg", "image/jpeg", "data".getBytes());
        String result = movieService.uploadPoster(file);

        assertTrue(result.contains("poster.jpg"));
        Path path = Path.of("test-posters", result);
        assertTrue(Files.exists(path));

        // cleanup
        Files.deleteIfExists(path);
        Files.deleteIfExists(path.getParent());
    }

    @Test
    void uploadPoster_shouldThrowException_whenIOExceptionOccurs() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("poster.jpg");
        when(mockFile.getBytes()).thenThrow(new IOException("Simulated failure"));

        ServiceException exception = assertThrows(ServiceException.class, () -> movieService.uploadPoster(mockFile));

        assertTrue(exception.getMessage().contains("Failed to upload poster"));
    }


    @Test
    void getPoster_shouldReturnResource_whenFileExists() throws IOException {
        String filename = "poster.jpg";
        Path postersPath = Path.of("test-posters");
        Files.createDirectories(postersPath);
        Path filePath = postersPath.resolve(filename);
        Files.write(filePath, "mockImageData".getBytes());

        Resource resource = movieService.getPoster(filename);

        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        assertEquals(filePath.toUri(), resource.getURI());

        Files.deleteIfExists(filePath);
        Files.deleteIfExists(postersPath);
    }

    @Test
    void getPoster_shouldThrowException_whenFileMissing() {
        assertThrows(PosterNotFoundException.class, () -> movieService.getPoster("nonexistent.jpg"));
    }

    @Test
    void getPoster_shouldThrowException_whenMalformedUrl() {
        ReflectionTestUtils.setField(movieService, "postersDir", "::invalid path::");

        assertThrows(ServiceException.class, () -> movieService.getPoster("poster.jpg"));
    }
}
