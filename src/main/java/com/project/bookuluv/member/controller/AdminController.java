package com.project.bookuluv.member.controller;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "admin/admin";
    }

}
