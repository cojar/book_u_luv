package com.project.bookuluv.base.configuration;

import com.project.bookuluv.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthenticationConfig {
    @Autowired
    private MemberService memberService;
    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain jwtsecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.httpBasic().disable()
                .csrf(
                        csrf -> csrf.disable()
                )
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/users/login",
                        "/api/v1/users/join",
                        "/member/join",
                        "/member/login",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/**"
                ).permitAll()
                .requestMatchers("/admin/**").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers("/author/**").hasAnyRole("SUPERADMIN", "ADMIN", "AUTHOR")
                .requestMatchers("/member/**").authenticated() // 로그인만 필요
                .anyRequest().authenticated() // 기타 모든 요청에 대해서도 로그인만 필요
                .and()
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login") // GET
                                .loginProcessingUrl("/member/login") // POST
                                .defaultSuccessUrl("/")
                )
//                .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
//                .and()
//                .addFilterBefore(new JwtFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class)
//                .build();
        // OAuth 로그인
                .oauth2Login(
                oauth2Login -> oauth2Login
                        .loginPage("/member/login")
                        .defaultSuccessUrl("/")
                        .userInfoEndpoint(
                                userInfoEndpoint -> userInfoEndpoint
                                        .userService(oAuth2UserService)
                        )
        )
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        ;
        return httpSecurity.build();
    }
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
}
