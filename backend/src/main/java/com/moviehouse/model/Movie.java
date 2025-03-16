package com.moviehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.*;
import static com.moviehouse.util.constant.RegexConstant.POSTER_URL_REGEX;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = EMPTY_TITLE)
    @Size(max = 100, message = TITLE_SIZE_EXCEEDED)
    private String title;

    @NotBlank(message = EMPTY_DESCRIPTION)
    @Size(max = 500, message = DESCRIPTION_SIZE_EXCEEDED)
    private String description;

    @NotNull(message = EMPTY_DURATION)
    @Min(value = 1, message = INVALID_DURATION)
    private Integer duration;

    @NotBlank(message = EMPTY_AGE_RATING)
    @Size(max = 10, message = AGE_RATING_SIZE_EXCEEDED)
    private String ageRating;

    @NotNull(message = EMPTY_RELEASE_DATE)
    private LocalDate releaseDate;

    @NotBlank(message = EMPTY_POSTER_URL)
    @Pattern(regexp = POSTER_URL_REGEX, message = INVALID_POSTER_URL)
    private String posterUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "last_modified")
    private LocalDateTime lastModified = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;
}
