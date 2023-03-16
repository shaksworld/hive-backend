package com.example.hive.service;

import com.example.hive.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String email);

    Optional<User> getUserByPasswordResetToken(String token);

}
