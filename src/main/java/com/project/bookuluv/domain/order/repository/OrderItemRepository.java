package com.project.bookuluv.domain.order.repository;

import com.project.bookuluv.domain.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByIsPaidTrueAndPayDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}