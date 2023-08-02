package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private Long expireMs = 1000 * 60 * 60L;

    private final MemberRepository memberRepository;

    public String login(String userName, String password) {
        // 인증과정 생략
        return JwtUtil.createJwt(userName, secretKey, expireMs);
    }
    public String join(String userName, String password) {
        // 중복체크
        memberRepository.findByUserName(userName)
                .ifPresent(member -> {
                    throw new RuntimeException(userName + "은 이미 있시유.");
                });
        // 저장하기(.save)
        Member member = Member.builder()
                .userName(userName)
                .password(password) // 나중에 암호화해서 저장(passwordEncoder)
                .build();
        memberRepository.save(member);
        return "SUCCESS";
    }
}
