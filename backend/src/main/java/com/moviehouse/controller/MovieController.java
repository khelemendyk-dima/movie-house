package com.moviehouse.controller;

import com.moviehouse.dto.MovieDto;
import com.moviehouse.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movie", description = "Endpoints for managing movies")
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = "Get all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of movies.")
    })
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(@RequestParam(required = false) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(movieService.getUniqueMoviesByDate(date));
        }

        return ResponseEntity.ok(movieService.getAll());
    }

    @Operation(summary = "Get movie details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movie details."),
            @ApiResponse(responseCode = "404", description = "Movie not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getById(id));
    }

    @Operation(summary = "Create a new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the movie."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data.")
    })
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody @Valid MovieDto movieDto) {
        return ResponseEntity.ok(movieService.create(movieDto));
    }

    @Operation(summary = "Update movie details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the movie."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "404", description = "Movie not found.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id,
                                                @RequestBody @Valid MovieDto movieDto) {
        return ResponseEntity.ok(movieService.update(id, movieDto));
    }

    @Operation(summary = "Delete a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the movie."),
            @ApiResponse(responseCode = "404", description = "Movie not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MovieDto> deleteMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.delete(id));
    }


    @Operation(summary = "Upload movie poster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully uploaded the poster."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data.")
    })
    @PostMapping("/poster/upload")
    public ResponseEntity<String> uploadPoster(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileName = movieService.uploadPoster(file);

        String posterUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/movies/poster/" + fileName)
                .toUriString();

        return ResponseEntity.ok(posterUrl);
    }

    @Operation(summary = "Get movie poster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the poster."),
            @ApiResponse(responseCode = "404", description = "Poster not found.")
    })
    @GetMapping("/poster/{filename}")
    public ResponseEntity<Resource> getPoster(@PathVariable String filename) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(movieService.getPoster(filename));
    }
}

