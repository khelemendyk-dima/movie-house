package com.moviehouse.repository;

import com.moviehouse.model.MovieSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieSessionRepository extends JpaRepository<MovieSession, Long> {
}
