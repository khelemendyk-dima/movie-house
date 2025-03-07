package com.moviehouse.repository;

import com.moviehouse.model.Booking;
import com.moviehouse.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByIdAndStatus(Long id, BookingStatus status);
}
