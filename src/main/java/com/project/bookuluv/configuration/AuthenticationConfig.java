package com.project.bookuluv.configuration;

import com.project.bookuluv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final MemberService memberService;


    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain jwtsecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/users/login",
                        "/api/v1/users/join",
                        "/member/join",
                        "/member/login",
                        "/join",
                        "/login",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/**"
                ).permitAll()
                .and()
                .formLogin()
                .loginPage("/member/login") // 로그인 페이지를 지정해두면 스프링시큐리티의 통제를 받음(디버깅 과정에서 username을 못받아옴)
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .oauth2Login()
                .loginPage("/member/login")
                .and()
                .build();
//                .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
//                .and()
//                .addFilterBefore(new JwtFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class)
//                .build();
    }
}
