package com.project.bookuluv.domain.rebate.repository;

import com.project.bookuluv.domain.rebate.domain.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RebateItemRepository extends JpaRepository<RebateOrderItem, Long> {
    List<RebateOrderItem> findByIsPaidTrueAndPayDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}