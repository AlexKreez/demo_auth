package com.example.demo.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
//отправка почты
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    public void sendEmail(String mailTo, String password) {
        try {
            if (mailTo == null || mailTo.isBlank()) {
                throw new IllegalArgumentException("Ошибка: адрес получателя пустой");
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(mailTo);
            message.setSubject("Ваш пароль для входа в систему");
            message.setText("Здравствуйте!\n\nВаш сгенерированный пароль: " + password);

            mailSender.send(message);
            LOGGER.info("📩 Email отправлен на {}", mailTo);
        } catch (MailException e) {
            throw new RuntimeException("Ошибка отправки почты: " + e.getMessage(), e);
        }
    }
}

