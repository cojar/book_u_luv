package com.project.bookuluv.controller;

import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.repository.MemberRepository;
import com.project.bookuluv.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void t1() {
        Member member = new Member();
        member.setUserName("kikikiki@gmail.com");
        member.setPassword(passwordEncoder.encode("1234"));
        member.setFirstName("철수");
        member.setLastName("김");
        member.setPhone("010-1234-1234");
        memberRepository.save(member);
    }
}
