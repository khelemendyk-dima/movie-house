package com.moviehouse.service;

import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.dto.MovieSessionRegistrationDto;

import java.util.List;

public interface MovieSessionService {
    List<MovieSessionDto> getAll();
    MovieSessionDto getById(Long id);
    List<MovieSessionDto> create(MovieSessionRegistrationDto movieSessionDto);
    MovieSessionDto update(Long id, MovieSessionDto movieSessionDto);
    MovieSessionDto delete(Long id);
}
