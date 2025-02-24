package com.moviehouse.service.impl;

import com.moviehouse.dto.MovieDto;
import com.moviehouse.exceptions.*;
import com.moviehouse.model.Genre;
import com.moviehouse.model.Movie;
import com.moviehouse.repository.GenreRepository;
import com.moviehouse.repository.MovieRepository;
import com.moviehouse.service.MovieService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.FAILED_TO_LOAD_POSTER;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.FAILED_TO_UPLOAD_POSTER;
import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    @Value("${posters.upload.dir}")
    private String postersDir;

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ConvertorUtil convertor;

    @Transactional
    @Override
    public MovieDto create(MovieDto movieDto) {
        Movie movieToCreate = convertor.toMovie(movieDto);
        movieToCreate.setId(null);

        Set<Genre> genres = findGenresByNames(movieDto.getGenres());
        movieToCreate.setGenres(genres);

        movieRepository.save(movieToCreate);

        return convertor.toMovieDto(movieToCreate);
    }

    @Transactional
    @Override
    public MovieDto update(Long id, MovieDto movieDto) {
        Movie movieToUpdate = setupMovieToUpdate(id, movieDto);

        Set<Genre> genres = findGenresByNames(movieDto.getGenres());
        movieToUpdate.setGenres(genres);

        movieRepository.save(movieToUpdate);

        return convertor.toMovieDto(movieToUpdate);
    }

    @Transactional
    @Override
    public MovieDto delete(Long id) {
        Movie movie = findMovieById(id);

        movieRepository.delete(movie);

        return convertor.toMovieDto(movie);
    }

    @Override
    public List<MovieDto> getAll() {
        return movieRepository.findAll().stream()
                .map(convertor::toMovieDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDto getById(Long id) {
        Movie movie = findMovieById(id);

        return convertor.toMovieDto(movie);
    }

    @Override
    public String uploadPoster(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(postersDir, filename);

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new ServiceException(format(FAILED_TO_UPLOAD_POSTER, e.getMessage()));
        }

        return filename;
    }

    @Override
    public Resource getPoster(String filename) {
        try {
            Path filePath = Paths.get(postersDir, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new PosterNotFoundException(filename);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new ServiceException(format(FAILED_TO_LOAD_POSTER, e.getMessage()));
        }
    }

    private Movie setupMovieToUpdate(Long id, MovieDto movieDto) {
        Movie existingMovie = findMovieById(id);

        Movie movieToUpdate = convertor.toMovie(movieDto);
        movieToUpdate.setId(existingMovie.getId());

        return movieToUpdate;
    }

    private Movie findMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    private Set<Genre> findGenresByNames(Set<String> genreNames) {
        return genreNames.stream()
                .map(this::findGenreByName)
                .collect(Collectors.toSet());
    }

    private Genre findGenreByName(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseThrow(() -> new GenreNotFoundException(genreName));
    }
}
