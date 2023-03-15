package com.example.hive.service.implementation;

import com.example.hive.constant.enums.Role;
import com.example.hive.dto.Request.UserRegistrationRequestDto;
import com.example.hive.dto.Response.RegistrationResponseDto;
import com.example.hive.entity.User;
import com.example.hive.exceptions.ResourceCreationException;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public RegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        log.info("register user and create account");

            if (doesUserAlreadyExist(registrationRequestDto.getEmail())) {
                throw new ResourceCreationException("User already exist");
            }
            // generateToken and Save to token repo

            User newUser = saveNewUser(registrationRequestDto);

            //send email verfication link


            return mapToResponse(newUser);

    }

    public RegistrationResponseDto mapToResponse(User user) {
        return RegistrationResponseDto.builder()
                .email(user.getEmail())
                .build();
    }

    private User saveNewUser(UserRegistrationRequestDto registrationRequestDto) {
        User newUser = new User();
        Role role = registrationRequestDto.getRole();

        BeanUtils.copyProperties(registrationRequestDto, newUser);
        newUser.addRole(role);
        newUser.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));


        return userRepository.saveAndFlush(newUser);
    }


    private boolean doesUserAlreadyExist(String email) {
        return userRepository.getUserByEmail(email).isPresent();
    }

    }


