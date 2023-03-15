package com.example.hive.service.UserService;

import com.example.hive.dto.Response.RegistrationResponseDto;
import com.example.hive.dto.Request.UserRegistrationRequestDto;

public interface UserService {
    RegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto);
}
