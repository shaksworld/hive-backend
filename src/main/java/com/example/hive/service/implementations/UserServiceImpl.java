package com.example.hive.service.implementations;


import com.example.hive.constant.Role;
import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.RegistrationResponseDto;
import com.example.hive.entity.User;
import com.example.hive.exceptions.ResourceCreationException;
import com.example.hive.exceptions.ResourceNotFoundException;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReentrantLock lock = new ReentrantLock(true);

    @Override
    @Transactional
    public RegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        lock.lock();
        log.info("register user and create account");
        try {
            if (doesUserAlreadyExist(registrationRequestDto.getEmail())) {
                throw new ResourceCreationException("User already exist");
            }
            User newUser = saveNewUser(registrationRequestDto);
            return mapToResponse(newUser);
        } finally {
            lock.unlock();
        }
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("userService loadUserByUserName - email :: [{}] ::", email);
        log.info("User ==> [{}]", userRepository.getUserByEmail(email));
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(
                        () -> {
                            throw new ResourceNotFoundException("user does not exist");
                        }
                );

//        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
