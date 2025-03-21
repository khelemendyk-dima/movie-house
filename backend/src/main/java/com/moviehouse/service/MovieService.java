package com.moviehouse.service;

import com.moviehouse.dto.MovieDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    List<MovieDto> getUniqueMoviesByDate(LocalDate date);

    MovieDto create(MovieDto movieDto);
    MovieDto update(Long id, MovieDto movieDto);
    MovieDto delete(Long id);
    MovieDto getById(Long id);
    List<MovieDto> getAll();

    String uploadPoster(MultipartFile file);
    Resource getPoster(String filename);
}
