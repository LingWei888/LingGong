package cn.exitcode.day001.apicontect.controller.user.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SendEmail {
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendVerificationCode(String email, String content , String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("15632198267@163.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        try {
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("发送邮件失败：{}", e.getMessage());
            return false;
        }
    }

}