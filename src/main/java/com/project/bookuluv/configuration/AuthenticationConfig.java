package com.project.bookuluv.configuration;

import com.project.bookuluv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
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
    public SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> securityConfigurer() {
        return new SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http
                        .authorizeRequests()
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
                        .anyRequest().authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/member/login").defaultSuccessUrl("/")
                        .and()
                        .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .and()
//                        .oauth2Login()
//                        .defaultSuccessUrl("/")
//                        .failureUrl("/member/login")
//                        .and()
                        .csrf().disable()
                        .cors();
            }
        };


//                .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
//                .and()
//                .addFilterBefore(new JwtFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class)
//                .build();
    }
}
