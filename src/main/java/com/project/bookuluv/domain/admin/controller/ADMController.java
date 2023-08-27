package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Notice;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.service.AdminService;
import com.project.bookuluv.domain.admin.service.NoticeService;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.dto.MemberJoinRequest;
import com.project.bookuluv.domain.member.dto.MemberRole;
import com.project.bookuluv.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ADMController {

    private final MemberService memberService;

    private final NoticeService noticeService;

    private final ProductService productService;

    private final AdminService adminService;

    @GetMapping("/admin/signup")
    public String showAdminSignup(MemberJoinRequest memberJoinRequest) {
        return "member/admin_signup"; // 관리자 회원가입 페이지로 이동
    }

    @PostMapping("/admin/signup")
    public String adminSignup(@Valid MemberJoinRequest dto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        try {
            // 인증 코드 검증
            if (dto.getMailKey().equals(dto.getGenMailKey())) {
                LocalDateTime currentDate = LocalDateTime.now();
                MemberRole role = MemberRole.SUPERADMIN; // TODO : 추후 MemberRole.ADMIN; 으로 할당값 변경
                this.adminService.createAdminMember(
                        dto.getUserName(),
                        dto.getPassword1(),
                        dto.getNickName(),
                        dto.getRoadAddress(),
                        dto.getJibunAddress(),
                        dto.getDetailAddress(),
                        dto.getExtraAddress(),
                        dto.getPostalNum(),
                        dto.getPhone(),
                        dto.getFirstName(),
                        dto.getLastName(),
                        dto.getGender(),
                        dto.getBirthDate(),
                        dto.getMailKey(),
                        role,
                        currentDate,
                        true,
                        dto.getAdminKey()
                );
            } else {
                // 두 값이 일치하지 않는 경우
                // 예외 처리 로직을 실행합니다.
                bindingResult.rejectValue("mailKey", "mailKeyNotMatched", "유효하지 않은 이메일 또는 메일 키입니다.");
                return "member/admin_signup";
            }
            return "redirect:/"; // 관리자 대시보드 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "잘못된 암호 키입니다.");
            return "redirect:/admin/signup"; // 잘못된 암호 키 경우, 다시 회원가입 페이지로 리다이렉트
        }
    }

    @GetMapping("/admin/member")
    @PreAuthorize("isAuthenticated()")
    public String adminMember(Model model) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        List<Member> memberList = this.memberService.getAll();
        model.addAttribute("memberList", memberList);
        return "/admin/member";
    }



    @PostMapping("/admin/member/update-role")
    @PreAuthorize("isAuthenticated()")
    public String updateMemberRole(@RequestParam Long memberId, @RequestParam MemberRole newRole) {
        if (!userIsAdmin()) { // 슈퍼 관리자만 가능
            throw new AccessDeniedException("Access is denied");
        }
        // userId와 newRole을 사용하여 해당 유저의 권한을 업데이트하는 로직 작성
        memberService.updateMemberRole(memberId, newRole);
        return "redirect:/admin/member"; // 회원 목록 페이지로 리다이렉트
    }

    // 회원 활성화 / 비활성화 PostMapping
    @PostMapping("/admin/member/toggle-active")
    @PreAuthorize("isAuthenticated()")
    public String toggleMemberActive(@RequestParam Long memberId) {
        if (!userIsSuperAdmin()) { // 모든 관리자권한 가능 TODO : 추후 슈퍼관리자만 가능으로 변경
            throw new AccessDeniedException("Access is denied");
        }
        memberService.toggleMemberActive(memberId);
        return "redirect:/admin/member"; // 회원 목록 페이지로 리다이렉트
    }

    // 회원 삭제(HARD DELETE) PostMapping
    @PostMapping("/admin/member/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteMember(@RequestParam Long memberId) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        memberService.deleteMember(memberId);
        return "redirect:/admin/member"; // 회원 목록 페이지로 리다이렉트
    }

    @GetMapping("/admin/domestic")
    @PreAuthorize("isAuthenticated()")
    public String adminDomestic(Model model,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "kw", defaultValue = "") String kw) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        if (page <= 0) {
            return "redirect:/admin/domestic?page=1";
        }
        Page<Product> products = this.productService.getDomesticProducts(page, kw);


        model.addAttribute("kw", kw);
        model.addAttribute("products", products);

        return "admin/domestic";
    }

    @GetMapping("/admin/foreign")
    @PreAuthorize("isAuthenticated()")
    public String adminForeign(Model model,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "kw", defaultValue = "") String kw) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        if (page <= 0) {
            return "redirect:/admin/foreign?page=1";
        }
        Page<Product> products = this.productService.getForeignProducts(page, kw);


        model.addAttribute("kw", kw);
        model.addAttribute("products", products);

        return "admin/foreign";
    }


    @GetMapping("/admin/notice")
    @PreAuthorize("isAuthenticated()")
    public String adminNotice(Model model) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        List<Notice> noticeList = this.noticeService.getAll();
        model.addAttribute("noticeList", noticeList);
        return "admin/notice";
    }
    private boolean hasAuthority(String... roles) { // String...은 Java에서 가변 인자(variable arity)를 나타내는 문법.
        // String...과 같이 선언하면, 해당 메서드를 호출할 때 여러 개의 인자를 전달할 수 있고, 이렇게 전달된 인자들은 배열로 처리됨.(roles 는 배열변수로써 권한정보를 문자열로 담고 있다.)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> Arrays.stream(roles)
                        .anyMatch(role -> authority.getAuthority().equals("ROLE_" + role)));
    }

    private boolean userIsSuperAdmin() {
        return hasAuthority("SUPERADMIN");
    }

    boolean userIsAdmin() {
        return hasAuthority("SUPERADMIN", "ADMIN");
    }

    boolean userIsAuthor() {
        return hasAuthority("SUPERADMIN", "ADMIN", "AUTHOR");
    }

    boolean userHasAnyRole() {
        return hasAuthority("SUPERADMIN", "ADMIN", "AUTHOR", "MEMBER");
    }
}
