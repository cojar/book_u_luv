package com.project.bookuluv.domain.api.repository;

import com.project.bookuluv.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Member, Long> {
}
