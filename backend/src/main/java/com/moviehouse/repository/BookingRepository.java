package com.moviehouse.repository;

import com.moviehouse.model.Booking;
import com.moviehouse.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByIdAndStatus(Long id, BookingStatus status);

    @Modifying
    @Transactional
    @Query("DELETE FROM Booking b WHERE b.status = :status AND b.createdAt < :expiredTime")
    int deleteExpiredBookings(LocalDateTime expiredTime, BookingStatus status);
}
