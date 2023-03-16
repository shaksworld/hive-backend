package com.example.hive.service.implementation;

import com.example.hive.entity.User;
import com.example.hive.repository.PasswordResetTokenRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {

        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).get().getUser());

    }
}
