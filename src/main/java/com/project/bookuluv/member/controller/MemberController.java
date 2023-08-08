package com.project.bookuluv.member.controller;

import com.project.bookuluv.mail.MailController;
import com.project.bookuluv.member.domain.Member;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MailController mailController;

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

    @GetMapping("/member/join")
    public String showSignup(MemberJoinRequest dto) {
        return "member/join";
    }

    @PostMapping("/member/join")
    public String signup(@Valid MemberJoinRequest dto) {
        this.memberService.join(dto.getUserName(), dto.getPassword1());
        return "redirect:/";
    }

    @GetMapping("/member/find")
    public String find() {
        return "find";
    }

    @GetMapping("/member/findUsername")
    public String showFindUsername() {
        return "/member/findUsername";
    }

    @PostMapping("/member/findUsername")
    public String findUsername(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("phone") String phone) {

        Member member = memberService.getMember(firstName, lastName);

        if (member != null) {
            mailController.sendEmailForId(member.getUserName(), member.getPhone());
            return "메일 발송 완료";
        }

        return "/member/findUsername";
    }

    @GetMapping("/member/findPassword")
    public String showFindPw() {
        return "/member/findPassword";
    }

    @PostMapping("/member/findPassword")
    public String findPw() {
        return "/member/findPassword";
    }


    @GetMapping("/member/me")
    public String me() {
        return "mypage";
    }

}
