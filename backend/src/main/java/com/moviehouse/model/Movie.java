package com.moviehouse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.*;

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
//    @Pattern(
//            regexp = "^(https?:\\/\\/)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*\\/?$",
//            message = "Invalid poster URL"
//    )
    private String posterUrl;
}
