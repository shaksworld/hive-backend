package com.example.hive.service;

import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.UserRegistrationResponseDto;
import com.example.hive.entity.User;
import com.example.hive.entity.VerificationToken;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional
    UserRegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto);

    Boolean validateRegistrationToken(String token);

    String generateVerificationToken(User user);

    VerificationToken generateNewToken(String oldToken);
}
