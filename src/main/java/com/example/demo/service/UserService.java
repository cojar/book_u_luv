package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60l;

    public String join(String userName, String password) {

        // userName 중복체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미있습니다.");
                });

        // 저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();

        userRepository.save(user);
        return "SUCCESS";
    }

    public String login(String userName, String password) {

        return JwtUtil.createJwt(userName, secretKey, expiredMs);
    }
}
