package com.example.hive.utils;

import com.example.hive.entity.User;
import com.example.hive.entity.VerificationToken;
import com.example.hive.repository.VerificationTokenRepository;
import com.example.hive.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
@RequiredArgsConstructor
@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final JavaMailSender mailSender;
    private final EmailService emailService;

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();

        try {
            // create and save verification token for user
            String token = generateVerificationToken(user);
            emailService.sendEmail(EmailTemplates.createVerificationEmail(user, token, event.getApplicationUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private  String generateVerificationToken(User user) {
        log.info("inside generateVerificationToken, generating token for {}", user.getEmail());
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);

        log.info("Saving token to database");
        verificationTokenRepository.save(verificationToken);
        return token;
    }
}

