package com.project.bookuluv.member.service;

import com.project.bookuluv.DataNotFoundException;
import com.project.bookuluv.member.exception.AppException;
import com.project.bookuluv.member.exception.ErrorCode;
import com.project.bookuluv.member.repository.MemberRepository;
import com.project.bookuluv.utils.JwtUtil;
import com.project.bookuluv.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60l;

    public String join(String userName, String password) {

        // userName 중복체크
        memberRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + " 는 이미있습니다.");
                });

        // 저장
        Member user = Member.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();

        memberRepository.save(user);
        return "SUCCESS";
    }

    public String login(String userName, String password) {

        // userName 없음
        Member user = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        // password 틀림
        // if (!encoder.matches(user.getPassword(), password)) {
        //    throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        //  }

        String token = JwtUtil.createJwt(user.getUserName(), secretKey, expiredMs);
        return token;
    }

    public Member getMember(String userName) {
        Optional<Member> siteUser = this.memberRepository.findByUserName(userName);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("member not found");
        }
    }

    public Member getMember(String firstName, String lastName) {
        Optional<Member> _member = this.memberRepository.findByUserFirstNameAndLastName(firstName, lastName);
        if (_member.isPresent()) {
            return _member.get();
        }
        throw new DataNotFoundException("member not found");
    }


    // public String me(String userName) {
    //  User user = memberRepository.findByUserName(userName)
    //           .orElseThrow();
    //   return ;
    // }
}
