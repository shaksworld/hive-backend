package com.example.hive.utils;

import com.example.hive.entity.User;
import com.example.hive.entity.VerificationToken;
import com.example.hive.repository.VerificationTokenRepository;
import com.example.hive.service.EmailService;
import com.example.hive.service.UserService;
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

    private final EmailService emailService;
    private final UserService userService;




    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();

        try {
            // create and save verification token for user
            String token = userService.generateVerificationToken(user);
            emailService.sendEmail(EmailTemplates.createVerificationEmail(user, token, event.getApplicationUrl()));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }



}

