package com.moviehouse.repository;

import com.moviehouse.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllBySessionId(Long sessionId);

    boolean existsByStatus(String status);
}
