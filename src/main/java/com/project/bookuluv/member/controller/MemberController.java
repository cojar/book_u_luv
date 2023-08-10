package com.project.bookuluv.member.controller;

import com.project.bookuluv.DataNotFoundException;
import com.project.bookuluv.mail.MailController;
import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberJoinRequest;
import com.project.bookuluv.member.dto.MemberLoginRequest;
import com.project.bookuluv.member.dto.MemberRole;
import com.project.bookuluv.member.service.MemberSecurityService;
import com.project.bookuluv.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberSecurityService memberSecurityService;

    private final MailController mailController;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/members/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest dto) {
        MemberRole role = dto.getUserName().startsWith("admin") ? MemberRole.ADMIN : MemberRole.USER;
        this.memberService.join(
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
                dto.isGender(),
                dto.getBirthDate(),
                dto.getMailKey(),
                role,
                true);
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
    public String showSignup(MemberJoinRequest memberJoinRequest) {
//        model.addAttribute("memberJoinRequest", new MemberJoinRequest());
        return "member/join";
    }


    @PostMapping("/member/join")
    public String signup(@Valid MemberJoinRequest dto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        if (!dto.getPassword1().equals(dto.getPassword2())) {
            bindingResult.rejectValue("password1", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "member/join";
        }
        try {
            // 인증 코드 검증
            if (dto.getMailKey().equals(dto.getGenMailKey())) {
                LocalDate currentDate = LocalDate.now();
                LocalDate birthDate = dto.getBirthDate();

                // 회원가입 처리
                MemberRole role = dto.getUserName().startsWith("admin") ? MemberRole.ADMIN : MemberRole.USER;
                this.memberService.join(
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
                        dto.isGender(),
                        dto.getBirthDate(),
                        dto.getMailKey(),
                        role,
                        true
                );
            } else {
                // 두 값이 일치하지 않는 경우
                // 예외 처리 로직을 실행합니다.
                bindingResult.rejectValue("mailKey", "mailKeyNotMatched", "유효하지 않은 이메일 또는 메일 키입니다.");
                return "member/join";
            }
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("⚠️회원가입에 실패했습니다⚠️", "이미 등록된 아이디입니다.");
            return "member/join";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("⚠️회원가입에 실패했습니다⚠️", e.getMessage());
            return "member/join";
        }
        return "redirect:/";
    }


    @GetMapping("/member/findUsername")
    public String findUsername() {
        return "member/findUsername";
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
        return "member/findPassword";
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
    @GetMapping("/member/mypage")
    public String myPage(Model model, Principal principal) {
        Member member = memberService.getUser(principal.getName());
//        List<Review> reviewList = reviewService.getReviewsByAuthor(user);
//        List<Product> voterProducts = productService.getProductsByVoter(user);
//        List<Product> wishProducts = productService.getProductsByWish(user);
//        model.addAttribute("voterProducts", voterProducts);
//        model.addAttribute("wishProducts", wishProducts);
//        model.addAttribute("reviewList", reviewList);
        model.addAttribute("userName", member.getUserName());
        model.addAttribute("userNickName", member.getNickName());
        model.addAttribute("userBirthDate", member.getBirthDate());
        model.addAttribute("userImg", member.getImgFilePath());

        return "member/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/member/updateprofile")
    public String updateProfileImg(@Valid @ModelAttribute("memberJoinRequest") MemberJoinRequest memberJoinRequest,
                                   BindingResult bindingResult,
                                   Principal principal,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletResponse response) throws Exception {
        Member member = this.memberService.getUser(principal.getName());

        // 업로드된 파일을 임시 폴더에 저장
        String tempFolderPath = System.getProperty("java.io.tmpdir");
        File tempFile = File.createTempFile("temp", file.getOriginalFilename(), new File(tempFolderPath));
        file.transferTo(tempFile);

        // 프로필 이미지 업데이트
        memberService.updateProfile(member, tempFile);
        // TODO : 이미지파일 불러올 때 안정적으로 불러올 수 있도록 처리예정.

        // 이미지 캐싱 방지를 위한 헤더 설정
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        redirectAttributes.addFlashAttribute("successMessage", "프로필 이미지가 업데이트되었습니다.");

        return "redirect:/";
    }


    @GetMapping("/member/me")
    public String me() {
        return "profile";
    }
    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @PostMapping("/member/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session, Model model) {

            UserDetails userDetails = memberSecurityService.loadUserByUsername(userName);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            return "redirect:/";
    }

}
