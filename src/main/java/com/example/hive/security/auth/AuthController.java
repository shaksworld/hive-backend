package com.example.hive.security.auth;

import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.UserRegistrationResponseDto;
import com.example.hive.exceptions.CustomException;
import com.example.hive.security.JwtService;
import com.example.hive.security.TokenResponse;
import com.example.hive.service.UserService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
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
import java.util.List;

import static com.example.hive.constant.SecurityConstants.PASSWORD_NOT_MATCH_MSG;
import static com.example.hive.utils.StringUtil.doesBothStringMatch;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest request) {
        log.info("controller login: login user :: [{}] ::", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (authentication.isAuthenticated()) {
            TokenResponse tokenResponse = JwtService.generateToken(authentication);
            return ResponseEntity.status(200).body(AppResponse.builder().statusCode("00").isSuccessful(true).result(tokenResponse).message("Authenticated").build());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
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
