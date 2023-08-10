package com.project.bookuluv.controller;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberRole;
import com.project.bookuluv.member.repository.MemberRepository;
import com.project.bookuluv.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 테스트, 카카오 주소검색 적용 후 - 인성 회원가입")
    void t1() {
        Member member = new Member();
        member.setUserName("insung5555@gmail.com");
        member.setPassword(passwordEncoder.encode("1234"));
        member.setNickName("간지폭풍");
        member.setRoadAddress("대전 유성구 계룡로 84");
        member.setJibunAddress("대전 유성구 봉명동 447-6");
        member.setDetailAddress("1213호");
        member.setExtraAddress(" (봉명동)");
        member.setPostalNum("34179");
        member.setPhone("010-1234-1234");
        member.setFirstName("인성");
        member.setLastName("황");
        member.setGender(true); // true : 남자, false : 여자
        member.setBirthDate(LocalDate.ofEpochDay(1993-11-03));
        member.setMailKey(34256);
        member.setRole(MemberRole.valueOf("USER"));
        member.setMailAuth(true); // 메일 인증 여부
        member.setCreateDate(LocalDate.from(LocalDateTime.now()));

        this.memberRepository.save(member);
    }

    @Test
    @DisplayName("회원가입 테스트, 카카오 주소검색 적용 후 - 창규 회원가입")
    void t2() {
        Member member = new Member();
        member.setUserName("changgyu549@gmail.com");
        member.setPassword(passwordEncoder.encode("1234"));
        member.setNickName("창규는 못말려");
        member.setRoadAddress("대전 유성구 계룡로 84");
        member.setJibunAddress("대전 유성구 봉명동 447-6");
        member.setDetailAddress("1214호");
        member.setExtraAddress(" (봉명동)");
        member.setPostalNum("34179");
        member.setPhone("010-1234-1234");
        member.setFirstName("창규");
        member.setLastName("박");
        member.setGender(true); // true : 남자, false : 여자
        member.setBirthDate(LocalDate.ofEpochDay(1993-11-03));
        member.setMailKey(34256);
        member.setRole(MemberRole.valueOf("USER"));
        member.setMailAuth(true); // 메일 인증 여부
        member.setCreateDate(LocalDate.from(LocalDateTime.now()));

        this.memberRepository.save(member);
    }
}
