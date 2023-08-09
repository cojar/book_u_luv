package com.project.bookuluv.member.controller;

import com.project.bookuluv.DataNotFoundException;
import com.project.bookuluv.mail.MailController;
import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberJoinRequest;
import com.project.bookuluv.member.dto.MemberLoginRequest;
import com.project.bookuluv.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MailController mailController;

    private final PasswordEncoder passwordEncoder;

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


    @GetMapping("/member/findUsername")
    public String findUsername() {
        return "/member/findUsername";
    }

    @PostMapping("/member/findUsername")
    @ResponseBody
    public ResponseEntity<String> findUsername(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("phone") String phone) {

        Member member = memberService.getMember(firstName, lastName, phone);

        if (member != null) {
            String result = "찾은 아이디 : " + member.getUserName();
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾지 못했습니다.");
        }
    }

    @GetMapping("/member/findPassword")
    public String showFindPw() {
        return "/member/findPassword";
    }

    @PostMapping("/member/findPassword")
    @ResponseBody
    public String findPw(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("phone") String phone, @RequestParam("userName") String userName) {

        try {
            Member member = memberService.getMember(firstName, lastName, phone, userName);

            mailController.sendEmailForPw(userName, lastName, firstName, phone);

            return "success";

        } catch (DataNotFoundException e) {
            return "fail";
        }
    }

    @PostMapping("/mypage/changePassword")
    @ResponseBody
    public String changePassword(@RequestParam("newpassword") String newpw, @RequestParam("newpasswordcf") String newpwcf) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberService.getMember(username);

        if (!newpw.equals(newpwcf)) {
            return "변경할 비밀번호와 확인 비밀번호가 일치하지 않습니다.";
        }
        member.setPassword(passwordEncoder.encode(newpw));
        memberService.saveMember(member);
        return "/";
    }

    @GetMapping("/member/me")
    public String me() {
        return "mypage";
    }

}
