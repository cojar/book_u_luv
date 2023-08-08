package com.project.bookuluv.mail;

import com.project.bookuluv.DataNotFoundException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MailController {

    @PostMapping("")
    @ResponseBody
    public void sendEmailForId(@RequestParam("userEmail") String userEmail, String userName) {
        String from = "admin@ToolTool.com";//보내는 이 메일주소
        String to = userEmail;
        String title = "아이디 찾기 결과입니다.";
        String content = "[아이디] " + userName + " 입니다. <br/>";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

        } catch (Exception e) {
            throw new DataNotFoundException("error");
        }
    }
}
