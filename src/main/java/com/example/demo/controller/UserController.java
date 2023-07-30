package com.example.demo.controller;

import com.example.demo.domain.LoginRequest;
import com.example.demo.dto.UserJoinRequest;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest dto) {
        userService.join(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok().body(userService.login(dto.getUserName(), ""));
    }


}
