package com.project.bookuluv.api.repository;

import com.project.bookuluv.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Member, Long> {
}
