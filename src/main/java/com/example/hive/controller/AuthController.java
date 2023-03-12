package com.example.hive.controller;


import com.example.hive.dto.request.LogInRequest;
import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.RegistrationResponseDto;
import com.example.hive.dto.response.TokenResponse;
import com.example.hive.service.UserService;
import com.example.hive.utils.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @PostMapping(path = "/register")
    public ResponseEntity<AppResponse<?>> registerUser(@RequestBody @Valid final UserRegistrationRequestDto registrationRequestDto) {
        log.info("controller register: register user :: [{}] ::", registrationRequestDto.getEmail());
        RegistrationResponseDto response = userService.registerUser(registrationRequestDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/auth/register").toUriString());
        return ResponseEntity.created(uri).body(AppResponse.buildSuccess(response));
    }



    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LogInRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (authentication.isAuthenticated()) {
            TokenResponse tokenResponse = SecurityUtil.generateToken(authentication);
            return ResponseEntity.status(200).body(AppResponse.builder().statusCode("00").isSuccessful(true).result(tokenResponse).message("Authenticated").build());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
