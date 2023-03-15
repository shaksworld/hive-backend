package com.example.hive.service.UserService;

import com.example.hive.dto.response.UserRegistrationResponseDto;
import com.example.hive.dto.request.UserRegistrationRequestDto;

public interface UserService {
    UserRegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto);
}
