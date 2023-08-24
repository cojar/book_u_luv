package com.project.bookuluv.domain.member.repository;

import com.project.bookuluv.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 회원 ID로 조회
    Optional<Member> findById(Long id);

    // 사용자 이름(이메일)로 조회
    Optional<Member> findByUserName(String userName);

    Optional<Member> findByFirstNameAndLastNameAndPhone(String firstName, String lastName, String phone);

    Optional<Member> findByFirstNameAndLastNameAndPhoneAndUserName(String firstName, String lastName, String phone, String userName);
//    @Modifying
//    @Transactional
//    @Query("UPDATE Member u SET u.mailAuth = true WHERE u.username = :username AND u.mailKey = :mailKey")
//    int updateMailAuth(@Param("username") String userName, @Param("mailKey") int mailKey);
}
