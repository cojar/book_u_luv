package com.example.demo.controller;

import com.example.demo.domain.dto.MemberSignupRequest;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberSignupRequest dto) {
        memberService.join(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body("회원가입 완료!뿌잉뿌잉");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok().body(memberService.login("", ""));
    }

}
