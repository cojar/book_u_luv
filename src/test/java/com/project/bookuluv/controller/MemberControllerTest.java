package com.project.bookuluv.controller;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.repository.MemberRepository;
import com.project.bookuluv.member.service.MemberService;
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
        member.setUserName("changgyu549@gmail.com");
        member.setPassword(passwordEncoder.encode("1234"));
        member.setFirstName("창규");
        member.setLastName("박");
        member.setPhone("010-4277-0595");
        memberRepository.save(member);
    }
}
