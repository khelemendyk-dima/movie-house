package com.moviehouse.repository;

import com.moviehouse.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("""
                SELECT t FROM Ticket t
                JOIN t.booking b
                JOIN t.seat s
                WHERE t.session.id = :sessionId AND b.status = 'PAID'
            """)
    List<Ticket> findPaidTicketsBySessionId(@Param("sessionId") Long sessionId);
}
