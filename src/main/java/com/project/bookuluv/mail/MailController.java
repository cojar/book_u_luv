package com.project.bookuluv.mail;

import com.project.bookuluv.DataNotFoundException;
import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.repository.MemberRepository;
import com.project.bookuluv.member.service.MemberService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final JavaMailSender mailSender;

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/member/findPw/sendEmail")
    @ResponseBody
    public void sendEmailForPw(@RequestParam("userName") String userName, String firstName, String lastName, String phone) {

        String tempPw = memberService.generateTempPassword();
        String from = "admin@ToolTool.com";//보내는 이 메일주소
        String to = userName;
        String title = "임시 비밀번호입니다.";
        String content = lastName + firstName + "님의" + "[임시 비밀번호] " + tempPw + " 입니다. <br/> 접속한 후 비밀번호를 변경해주세요";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

            Member member = memberService.getMember(userName, firstName, lastName);
            member.setPassword(passwordEncoder.encode(tempPw));
            memberRepository.save(member);

        } catch (Exception e) {
            throw new DataNotFoundException("error");
        }
    }

    @GetMapping("/mailCheck")
    @ResponseBody
    public int processMailCheck(@RequestParam("email") String email) throws Exception {
        int mailKey = (int) ((Math.random() * (99999 - 10000 + 1)) + 10000);

        String to = email;
        String title = "안녕하세요 주류추천 서비스 Spirits 입니다. 회원가입시 필요한 인증번호 입니다.";
        String content = "[인증번호] " + mailKey + " 입니다. <br/> 인증번호 확인란에 기입해주십시오.";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

        } catch (Exception e) {
            throw new DataNotFoundException("error");
        }
        return mailKey;
    }
}
