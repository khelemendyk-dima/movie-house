package com.moviehouse.service;

import com.moviehouse.dto.MovieDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    List<MovieDto> getAllMovies();

    List<MovieDto> getAllUniqueMoviesByStartDate(LocalDate startDate);

    MovieDto getMovieById(Long id);

    MovieDto createMovie(MovieDto movieDto);

    MovieDto updateMovie(Long id, MovieDto movieDto);

    MovieDto deleteMovie(Long id);

    String uploadPoster(MultipartFile file);

    Resource getPoster(String filename);
}
