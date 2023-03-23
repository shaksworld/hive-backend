package com.example.hive.utils;

import com.example.hive.dto.request.EmailDto;
import com.example.hive.entity.User;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class EmailTemplates {

    private static final String senderCredential = "hive@blessingchuks.tech";

    public static EmailDto createVerificationEmail(User recipient,String token, String eventUrl) {
        String verificationUrl = eventUrl
                + "/verifyRegistration?token="
                + token;

        String mailContent = "<p> Dear "+ recipient.getFullName() +", </p>";
        mailContent += "<p> Please click the link below to verify your registration, </p>";
        mailContent += "<h3><a href=\""+ verificationUrl + "\"> VERIFY </a></h3>";
        mailContent += "<p>Thank you <br/> Hive team </p>";


        log.info("Link created {}", verificationUrl);
        return EmailDto.builder()
                .sender(senderCredential)
                .subject("Please Activate Your Account")
                .body(mailContent)
                .recipient(recipient.getEmail())
                .build();


    }

    public static EmailDto createPaymentVerificationCodeMail(User recipient, String reference) {

        String mailContent = "<p> Dear \"" + recipient.getFullName() +  "\", your Verification code is \"" + reference + "\"</p>";

        return EmailDto.builder()
                .sender(senderCredential)
                .subject("Payment Verification Code")
                .body(mailContent)
                .recipient(recipient.getEmail())
                .build();
    }

}
