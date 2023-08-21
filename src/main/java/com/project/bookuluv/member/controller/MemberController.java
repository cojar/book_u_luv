package com.project.bookuluv.member.controller;

import com.project.bookuluv.mail.MailController;
import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberJoinRequest;
import com.project.bookuluv.member.dto.MemberLoginRequest;
import com.project.bookuluv.member.dto.MemberRole;
import com.project.bookuluv.member.dto.MemberUpdateRequest;
import com.project.bookuluv.member.exception.DataNotFoundException;
import com.project.bookuluv.member.service.MemberSecurityService;
import com.project.bookuluv.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/updateprofile")
    public String profileModify(@ModelAttribute("memberUpdateRequest") MemberUpdateRequest memberUpdateRequest,
                                Principal principal,
                                Model model) {
        Member member = memberService.getUser(principal.getName());
        if (!member.getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        // 기존 회원정보를 회원정보 수정 입력창에 세팅
        memberUpdateRequest.setUserName(member.getUserName());
        memberUpdateRequest.setNickName(member.getNickName());
        memberUpdateRequest.setPhone(member.getPhone());
        memberUpdateRequest.setFirstName(member.getFirstName());
        memberUpdateRequest.setLastName(member.getLastName());
        memberUpdateRequest.setGender(member.getGender());
        memberUpdateRequest.setBirthDate(member.getBirthDate());
        memberUpdateRequest.setPostalNum(member.getPostalNum());
        memberUpdateRequest.setRoadAddress(member.getRoadAddress());
        memberUpdateRequest.setJibunAddress(member.getJibunAddress());
        memberUpdateRequest.setDetailAddress(member.getDetailAddress());
        memberUpdateRequest.setExtraAddress(member.getExtraAddress());

        // 소셜 계정인 경우 userName 가공
        if (member.getUserName().startsWith("KAKAO_")) {
            memberUpdateRequest.setUserName(member.getUserName().substring(member.getUserName().indexOf("_") + 1));
        } else if (member.getUserName().startsWith("NAVER_")) {
            memberUpdateRequest.setUserName(member.getUserName().substring(member.getUserName().indexOf("_") + 1));
        } else if (member.getUserName().startsWith("GOOGLE_")) {
            memberUpdateRequest.setUserName(member.getUserName().substring(member.getUserName().indexOf("_") + 1));
        }

        model.addAttribute("member", member);

        return "member/updateProfile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/member/updateProfileImg")
    @ResponseBody
    public ResponseEntity<String> updateProfileImg(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            Member member = memberService.getUser(principal.getName());
            memberService.updateProfile(member, file);
            return ResponseEntity.ok("프로필 이미지가 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 이미지 업데이트 중 오류가 발생했습니다.");
        }
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/changePassword")
    public String showModifyPassword(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberService.getMember(username);
        if (username.startsWith("KAKAO_") || username.startsWith("NAVER_") || username.startsWith("GOOGLE_")) {
            request.setAttribute("isSocialAccount", "true"); // 소셜 계정 여부를 전달
            return "redirect:/"; // 비밀번호 변경 페이지 로드
        }
        request.setAttribute("isSocialAccount", "false"); // 소셜 계정이 아님을 전달
        return "member/modifyPassword";
    }

    @PostMapping("/mypage/changePassword")
    @ResponseBody
    public Map<String, Object> changePasswordAjax(@RequestParam("oldpassword") String oldPassword,
                                                  @RequestParam("newpassword") String newpw,
                                                  @RequestParam("newpasswordcf") String newpwcf) {
        Map<String, Object> resultMap = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberService.getMember(username);

        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            resultMap.put("success", false);
            resultMap.put("message", "기존 비밀번호가 일치하지 않습니다.");
        } else if (!newpw.equals(newpwcf)) {
            resultMap.put("success", false);
            resultMap.put("message", "새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        } else if (oldPassword.equals(newpw) || oldPassword.equals(newpwcf)) {
            resultMap.put("success", false);
            resultMap.put("message", "기존 비밀번호와 동일한 비밀번호를 사용할 수 없습니다.");
        } else {
            member.setPassword(passwordEncoder.encode(newpw));
            memberService.saveMember(member);
            resultMap.put("success", true);
        }

        return resultMap;
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/profile")
    public String myPage(Model model, Principal principal) {
        Member member = memberService.getUser(principal.getName());
        return "member/profile";
    }

    @GetMapping("/api/getUserName")
    public ResponseEntity<Map<String, String>> getUserName(Principal principal) {
        Map<String, String> response = new HashMap<>();

        // 사용자 아이디 정보 가져오기
        String userName = principal.getName();
        response.put("userName", userName);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/member/delete")
    public String deactivateMember(Principal principal) {
        Member member = memberService.getUser(principal.getName());
        memberService.deactivateMember(member);
        return "redirect:/member/logout"; // 로그아웃 후 리다이렉트
    }

//    @PostMapping("/member/login")
//    public String login(@RequestParam("userName") String userName, HttpSession session) {
//            UserDetails userDetails = memberSecurityService.loadUserByUsername(userName);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
//
//            return "redirect:/";
//    }

}
