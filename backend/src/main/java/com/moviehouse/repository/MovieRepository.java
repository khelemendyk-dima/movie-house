package com.moviehouse.repository;

import com.moviehouse.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByReleaseDateAfter(LocalDate date);

    List<Movie> findAllByAgeRating(String ageRating);
}