package com.mircroservice.unir.orders.repository;

import com.mircroservice.unir.orders.repository.model.OrderStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Long> {
    List<OrderStatusLog> findByOrderIdOrderByChangedAtDesc(Long orderId);
}

