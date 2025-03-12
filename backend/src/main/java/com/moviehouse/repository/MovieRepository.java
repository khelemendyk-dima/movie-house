package com.moviehouse.repository;

import com.moviehouse.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
            SELECT DISTINCT s.movie FROM MovieSession s
            WHERE DATE(s.startTime) = :date
            """)
    List<Movie> findUniqueMoviesByDate(@Param("date") LocalDate date);
}