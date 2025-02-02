package com.moviehouse.repository;

import com.moviehouse.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByHallId(Long hallId);

    boolean existsByHallIdAndRowNumberAndSeatNumber(Long hallId, Integer rowNumber, Integer seatNumber);
}
