package com.example.hive.service.implementation;

import com.example.hive.enums.Role;
import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.UserRegistrationResponseDto;
import com.example.hive.entity.User;
import com.example.hive.exceptions.CustomException;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;




    @Override
    @Transactional
    public UserRegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        log.info("register user and create account");

            if (doesUserAlreadyExist(registrationRequestDto.getEmail())) {
                throw new CustomException("User already exist", HttpStatus.FORBIDDEN);
            }
            // generateToken and Save to token repo

            User newUser = saveNewUser(registrationRequestDto);



//            return modelMapper.map(newUser, UserRegistrationResponseDto.class);
         return new UserRegistrationResponseDto();
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


