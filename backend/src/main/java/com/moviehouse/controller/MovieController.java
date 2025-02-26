package com.moviehouse.controller;

import com.moviehouse.dto.MovieDto;
import com.moviehouse.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody @Valid MovieDto movieDto) {
        return ResponseEntity.ok(movieService.create(movieDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id,
                                                @RequestBody @Valid MovieDto movieDto) {
        return ResponseEntity.ok(movieService.update(id, movieDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieDto> deleteMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.delete(id));
    }

    @PostMapping("/poster/upload")
    public ResponseEntity<String> uploadPoster(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileName = movieService.uploadPoster(file);

        String posterUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/movies/poster/" + fileName)
                .toUriString();

        return ResponseEntity.ok(posterUrl);
    }

    @GetMapping("/poster/{filename}")
    public ResponseEntity<Resource> getPoster(@PathVariable String filename) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(movieService.getPoster(filename));
    }
}

