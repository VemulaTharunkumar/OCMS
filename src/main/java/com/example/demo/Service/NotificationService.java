package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class NotificationService {

    private JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void setMailSender(String to,String subject,String text) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setFrom(fromEmail);
        sm.setTo(to);
        sm.setSubject(subject);
        sm.setText(text);

        mailSender.send(sm);
    }
}
