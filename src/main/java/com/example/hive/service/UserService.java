package com.example.hive.service;

import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.RegistrationResponseDto;

public interface UserService {
    RegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto);
}
