package com.projeto.authentication_api.services.impl;

import com.projeto.authentication_api.exceptions.AuthenticationException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Código de verificação");
            helper.setText("Seu código de verificação é: " + code, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new AuthenticationException("Falha ao enviar código por e-mail");
        }
    }
}

