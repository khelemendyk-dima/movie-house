package com.moviehouse.repository;

import com.moviehouse.dto.SeatStatusDto;
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

    @Query("""
                SELECT new com.moviehouse.dto.SeatStatusDto(
                    s.id, s.rowNumber, s.seatNumber,
                    CASE
                        WHEN t.id IS NOT NULL AND b.status IN ('PAID', 'PENDING') THEN 'RESERVED'
                        ELSE 'FREE'
                    END
                )
                FROM Seat s
                LEFT JOIN Ticket t ON s.id = t.seat.id AND t.session.id = :sessionId
                LEFT JOIN Booking b ON t.booking.id = b.id
                WHERE s.hall.id = (
                    SELECT ms.hall.id FROM MovieSession ms WHERE ms.id = :sessionId
                )
            """)
    List<SeatStatusDto> getSeatStatusesBySessionId(@Param("sessionId") Long sessionId);
}
