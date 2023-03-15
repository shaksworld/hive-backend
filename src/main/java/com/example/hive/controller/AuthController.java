package com.example.hive.controller;

import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.UserRegistrationResponseDto;
import com.example.hive.exceptions.CustomException;
import com.example.hive.service.UserService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.hive.constant.SecurityConstants.PASSWORD_NOT_MATCH_MSG;
import static com.example.hive.utils.StringUtil.doesBothStringMatch;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @PostMapping(path = "/register")
    public ResponseEntity<AppResponse<?>> registerUser(@RequestBody @Valid UserRegistrationRequestDto registrationRequestDto) {
        log.info("controller register: register user :: [{}] ::", registrationRequestDto.getEmail());
        validateUserRegistration(registrationRequestDto);
        UserRegistrationResponseDto response = userService.registerUser(registrationRequestDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/auth/register").toUriString());
        return ResponseEntity.created(uri).body(AppResponse.buildSuccess(response));
    }

    private void validateUserRegistration(UserRegistrationRequestDto registrationRequestDto) {
        log.info("validating user registration request for email :: {}", registrationRequestDto.getEmail());
        if (!doesBothStringMatch(registrationRequestDto.getConfirmPassword(), registrationRequestDto.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH_MSG, HttpStatus.BAD_REQUEST);
        }
        List<String> roleEnum = List.of("TASKER", "DOER");

        String role = String.valueOf(registrationRequestDto.getRole());
        if (role != null) {
            role = role.trim().toUpperCase();
            if (!roleEnum.contains(role)) {
                throw new CustomException("Invalid role, Options includes: TASKER, DOER");
            }
        }
        log.info("successful validation for user registration request for email :: {}", registrationRequestDto.getEmail());
    }
}
