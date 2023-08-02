package com.example.demo.api.repository;

import com.example.demo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Member, Long> {
}
