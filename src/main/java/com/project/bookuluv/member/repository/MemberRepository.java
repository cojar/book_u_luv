package com.project.bookuluv.member.repository;

import com.project.bookuluv.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserName(String userName);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Member u SET u.mailAuth = true WHERE u.username = :username AND u.mailKey = :mailKey")
//    int updateMailAuth(@Param("username") String userName, @Param("mailKey") int mailKey);
}
