package com.project.bookuluv.member.repository;

import com.project.bookuluv.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserName(String userName);

    Optional<Member> findByFirstNameAndLastNameAndPhone(String firstName, String lastName, String phone);

    Optional<Member> findByFirstNameAndLastNameAndPhoneAndUserName(String firstName, String lastName, String phone, String userName);
}
