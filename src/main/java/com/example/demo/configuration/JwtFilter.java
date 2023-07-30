package com.example.demo.configuration;

import com.example.demo.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // 이 클래스가 Jwt를 비교해서 검증하는 클래스임

    private final MemberService memberService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authentication : {}", authentication);

        if(authentication == null) {
            log.error("authentication : {}", authentication);
            filterChain.doFilter(request, response);
            return;
        }


        // Username Token에서 꺼내기
        String username = "";

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken
                        (username, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail을 넣어줍니다.
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
