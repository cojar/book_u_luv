package com.project.bookuluv.member.service;

import com.project.bookuluv.member.domain.CustomMember;
import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberRole;
import com.project.bookuluv.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;
    Member member = new Member();
    public boolean isAdmin() {
        // 관리자 여부를 판별하는 로직을 구현
        // 예: 관리자라면 true, 일반 사용자라면 false 반환
        return "admin@gmail.com".equals(member.getUserName()) ||
                "admin1@gmail.com".equals(member.getUserName()) ||
                "admin2@gmail.com".equals(member.getUserName()) ||
                "insung5189@gmail.com".equals(member.getUserName()) ||
                "tjqls2013@gmail.com".equals(member.getUserName()) ||
                "thdcodud01@gmail.com".equals(member.getUserName()) ||
                "gim156922@gmail.com".equals(member.getUserName()) ||
                "pintor.dev@gmail.com".equals(member.getUserName()) ||
                "insung5189youpri@gmail.com".equals(member.getUserName())
                ;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername's userName : " + userName);
        Optional<Member> memberOp = memberRepository.findByUserName(userName);
        Member member = memberOp.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!member.isActive()) {
            throw new DisabledException("해당 회원은 비활성화 되었습니다.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
        }

        return new CustomMember(
                member.getUserName(),
                member.getPassword(),
                member,
                authorities);
    }
}