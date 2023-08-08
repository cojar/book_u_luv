package com.project.bookuluv;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;


    @GetMapping("/")
    public String root (Model model, Principal principal) {
        if (principal != null) {
            Member member = this.memberService.getUser(principal.getName());
            model.addAttribute("member", member);
            model.addAttribute("userImg", member.getImgFilePath());

            return "main"; // ROOT로 접근했을 때 페이지가 해당 주소로 리다이렉트 되게끔 리턴.
        }
        return "main";
    }

}
