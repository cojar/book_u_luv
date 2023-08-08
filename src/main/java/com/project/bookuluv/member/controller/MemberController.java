package com.project.bookuluv.member.controller;

import com.project.bookuluv.member.dto.MemberJoinRequest;
import com.project.bookuluv.member.dto.MemberLoginRequest;
import com.project.bookuluv.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
//@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest dto) {
        memberService.join(dto.getUserName(), dto.getPassword1());
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/api/v1/members/login")
    public ResponseEntity<String> login(@RequestBody MemberLoginRequest dto) {
        return ResponseEntity.ok().body(memberService.login(dto.getUserName(), ""));
    }

    // @GetMapping("/me")
    // public ResponseEntity<String> me(@RequestBody MemberLoginRequest dto) {
    //       return ResponseEntity.ok().body(memberService.me(dto.getUserName()));
    //  }

    //    @PreAuthorize("isAnonymous()")
    @GetMapping("/member/join")
    public String showSignup(MemberJoinRequest dto) {
        return "member/join";
    }

    @PostMapping("/member/join")
    public String signup(@Valid MemberJoinRequest dto) {
        this.memberService.join(dto.getUserName(), dto.getPassword1());
        return "redirect:/";
    }

    @GetMapping("/member/me")
    public String me() {
        return "mypage";
    }

}
