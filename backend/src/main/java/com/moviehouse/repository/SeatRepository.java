package com.moviehouse.repository;

import com.moviehouse.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("""
        SELECT COUNT(s) > 0
        FROM Seat s
        JOIN Ticket t ON s.id = t.seat.id
        JOIN Booking b ON t.booking.id = b.id
        WHERE t.session.id = :sessionId
        AND s.id IN :seatIds
        AND b.status IN ('PAID', 'PENDING')
    """)
    boolean areSeatsTaken(@Param("sessionId") Long sessionId, @Param("seatIds") List<Long> seatIds);
}
