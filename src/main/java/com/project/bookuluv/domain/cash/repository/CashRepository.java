package com.project.bookuluv.domain.cash.repository;

import com.project.bookuluv.domain.cash.domain.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepository extends JpaRepository<CashLog, Long> {


}
