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
import com.project.bookuluv.domain.order.domain.OrderItem;
import com.project.bookuluv.domain.rebate.service.RebateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ADMController {

    private final MemberService memberService;

    private final NoticeService noticeService;

    private final ProductService productService;

    private final AdminService adminService;

    private final RebateService rebateService;

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
                MemberRole role = MemberRole.ADMIN;
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
    public String adminMember(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 로그인되지 않은 경우 또는 인증되지 않은 경우 에러 페이지를 리턴
            return "error_page";
        }

        boolean isAdminOrSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN") || authority.getAuthority().equals("ROLE_SUPERADMIN"));

        if (!isAdminOrSuperAdmin) {
            // 로그인은 되어 있지만, 관리자 권한이 없는 경우 에러 페이지를 리턴
            return "error_page";
        }

        // 로그인되어 있고, 관리자 권한이 있는 경우 원하는 작업을 수행
        List<Member> memberList = this.memberService.getAll();
        model.addAttribute("memberList", memberList);
        return "admin/member";
    }



    @PostMapping("/admin/member/update-role")
    @PreAuthorize("isAuthenticated()")
    public String updateMemberRole(@RequestParam Long memberId, @RequestParam MemberRole newRole) {
        if (!userIsAdmin()) { // 슈퍼 관리자만 가능
            return "error_page";
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
            return "error_page";
        }
        memberService.toggleMemberActive(memberId);
        return "redirect:/admin/member"; // 회원 목록 페이지로 리다이렉트
    }

    // 회원 삭제(HARD DELETE) PostMapping
    @PostMapping("/admin/member/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteMember(@RequestParam Long memberId) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            return "error_page";
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
            return "error_page";
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
            return "error_page";
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
    public String noticeListad(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "kw", defaultValue = "") String kw,
                             @RequestParam(value = "field", defaultValue = "title") String field) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            return "error_page";
        }
        if (page <= 0) {
            return "redirect:/notice/list?page=1";
        }
        Page<Notice> notices = this.noticeService.getNotices(page, kw, field);
        model.addAttribute("notices", notices);
        model.addAttribute("field", field);
        model.addAttribute("kw", kw);
        return "admin/notice";
    }

    @ModelAttribute("searchResultLabel")
    public String getSearchResultLabelad(@RequestParam(value = "field", defaultValue = "") String field) {
        if ("title".equals(field)) {
            return "제목검색결과";
        } else if ("content".equals(field)) {
            return "내용검색결과";
        } else if ("all".equals(field)) {
            return "통합검색결과";
        } else {
            return "";
        }
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

    @GetMapping("/admin/calculate")
    public String showRebate(Model model) {
        return "admin/calculate";
    }

    @GetMapping("/admin/perform-rebate")
    public String rebate(Model model) {
        long totalPaidAmount = rebateService.rebateForMonth();

        // 추가 코드: 상품 판매 정보 조회
        Map<Product, Integer> productSales = rebateService.getProductSalesForMonth();
        model.addAttribute("productSales", productSales);

        // 추가 코드: 회원별 구매 내역 조회
        Map<Member, List<OrderItem>> memberPurchase = rebateService.getMemberPurchaseForMonth();
        long totalAmount = 0;

        for (List<OrderItem> orderItems : memberPurchase.values()) {
            for (OrderItem orderItem : orderItems) {
                totalAmount += orderItem.getPayPrice();
            }
        }
        model.addAttribute("memberPurchase", memberPurchase);

        model.addAttribute("totalPaidAmount", totalPaidAmount);
        model.addAttribute("totalAmount", totalAmount);

        model.addAttribute("message", "1개월 동안의 정산이 수행되었습니다.");
        return "admin/calculate";
    }

    @GetMapping("/deleteDomesticAd/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteDomestic(@PathVariable("id") Long id) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            return "error_page";
        }
        this.productService.delete(id);
        return "redirect:/admin/domestic";
    }

    @GetMapping("/deleteForeignAd/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteForeign(@PathVariable("id") Long id) {
        if (!userIsAdmin()) { // 모든 관리자권한 가능
            return "error_page";
        }
        this.productService.delete(id);
        return "redirect:/admin/foreign";
    }

}