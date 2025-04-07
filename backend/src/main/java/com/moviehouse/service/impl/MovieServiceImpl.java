package com.moviehouse.service.impl;

import com.moviehouse.dto.MovieDto;
import com.moviehouse.exception.*;
import com.moviehouse.model.Genre;
import com.moviehouse.model.Movie;
import com.moviehouse.repository.GenreRepository;
import com.moviehouse.repository.MovieRepository;
import com.moviehouse.service.MovieService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.FAILED_TO_LOAD_POSTER;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.FAILED_TO_UPLOAD_POSTER;
import static java.lang.String.format;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    @Value("${posters.upload.dir}")
    private String postersDir;

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ConvertorUtil convertor;

    @Override
    public List<MovieDto> getAllMovies() {
        log.info("Fetching all movies");

        return movieRepository.findAll().stream()
                .map(convertor::toMovieDto)
                .toList();
    }

    @Override
    public List<MovieDto> getAllUniqueMoviesByStartDate(LocalDate startDate) {
        log.info("Fetching unique movies with start date={}", startDate);

        return movieRepository.findAllUniqueMoviesStartByDate(startDate).stream()
                .map(convertor::toMovieDto)
                .toList();
    }

    @Override
    public MovieDto getMovieById(Long id) {
        log.info("Fetching movie with id={}", id);

        Movie movie = findMovieById(id);

        return convertor.toMovieDto(movie);
    }

    @Transactional
    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        log.info("Creating a new movie: {}", movieDto);

        Movie movieToCreate = convertor.toMovie(movieDto);
        movieToCreate.setId(null);

        Set<Genre> genres = findGenresByNames(movieDto.getGenres());
        movieToCreate.setGenres(genres);

        movieRepository.save(movieToCreate);

        log.info("Movie created successfully with id={}", movieToCreate.getId());

        return convertor.toMovieDto(movieToCreate);
    }

    @Transactional
    @Override
    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        log.info("Updating movie with id={}", id);

        Movie movieToUpdate = setupMovieToUpdate(id, movieDto);

        Set<Genre> genres = findGenresByNames(movieDto.getGenres());
        movieToUpdate.setGenres(genres);

        movieRepository.save(movieToUpdate);

        log.info("Movie updated successfully with title={}", movieToUpdate.getTitle());

        return convertor.toMovieDto(movieToUpdate);
    }

    @Transactional
    @Override
    public MovieDto deleteMovie(Long id) {
        log.info("Deleting movie with id={}", id);

        Movie movie = findMovieById(id);

        movieRepository.delete(movie);

        log.info("Movie deleted successfully with title={}", movie.getTitle());
        return convertor.toMovieDto(movie);
    }

    @Override
    public String uploadPoster(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.info("Uploading poster with filename={}", filename);

        Path filePath = Paths.get(postersDir, filename);

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            log.info("Poster uploaded successfully with filename={}", filename);
        } catch (IOException e) {
            throw new ServiceException(format(FAILED_TO_UPLOAD_POSTER, e.getMessage()));
        }

        return filename;
    }

    @Override
    public Resource getPoster(String filename) {
        log.info("Fetching poster with filename: {}", filename);

        try {
            Path filePath = Paths.get(postersDir, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new PosterNotFoundException(filename);
            }

            log.info("Poster fetched successfully with filename={}", filename);
            return resource;
        } catch (MalformedURLException e) {
            throw new ServiceException(format(FAILED_TO_LOAD_POSTER, e.getMessage()));
        }
    }

    private Movie setupMovieToUpdate(Long id, MovieDto movieDto) {
        log.info("Setting up movie to update for movie with id={}", id);

        Movie existingMovie = findMovieById(id);

        Movie movieToUpdate = convertor.toMovie(movieDto);
        movieToUpdate.setId(existingMovie.getId());

        return movieToUpdate;
    }

    private Movie findMovieById(Long id) {
        log.debug("Searching for movie with id={}", id);

        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    private Set<Genre> findGenresByNames(Set<String> genreNames) {
        log.debug("Fetching genres for genre names: {}", genreNames);

        return genreNames.stream()
                .map(this::findGenreByName)
                .collect(Collectors.toSet());
    }

    private Genre findGenreByName(String genreName) {
        log.debug("Searching for genre with name={}", genreName);

        return genreRepository.findByName(genreName)
                .orElseThrow(() -> new GenreNotFoundException(genreName));
    }
}
