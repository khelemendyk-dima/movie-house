package com.moviehouse.repository;

import com.moviehouse.model.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {

    List<ReservedSeat> findAllByOrderId(Long orderId);

    List<ReservedSeat> findAllBySeatId(Long seatId);

    boolean existsBySeatIdAndOrderId(Long seatId, Long orderId);
}
