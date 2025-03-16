package com.moviehouse.repository;

import com.moviehouse.model.MovieSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieSessionRepository extends JpaRepository<MovieSession, Long> {
    @Query("""
            SELECT s FROM MovieSession s
            JOIN FETCH s.movie
            WHERE s.movie.id = :movieId AND DATE(s.startTime) = :date
            ORDER BY s.startTime ASC
            """)
    List<MovieSession> findAllByMovieIdAndDate(@Param("movieId") Long movieId, @Param("date") LocalDate date);

    @Query("""
            SELECT s FROM MovieSession s
            JOIN FETCH s.movie
            WHERE s.movie.id = :movieId AND s.startTime > :dateTime
            ORDER BY s.startTime ASC
            """)
    List<MovieSession> findAllByMovieIdAndStartTimeAfter(@Param("movieId") Long movieId, @Param("dateTime") LocalDateTime dateTime);
}
