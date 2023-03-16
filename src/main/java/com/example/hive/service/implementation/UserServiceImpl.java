package com.example.hive.service.implementation;

<<<<<<< HEAD

import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.dto.response.UserRegistrationResponseDto;
import com.example.hive.entity.User;
import com.example.hive.repository.PasswordResetTokenRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.hive.entity.Address;
import com.example.hive.entity.User;
import com.example.hive.entity.VerificationToken;
import com.example.hive.enums.Role;
import com.example.hive.exceptions.CustomException;
import com.example.hive.repository.AddressRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.repository.VerificationTokenRepository;
import com.example.hive.service.UserService;
import com.example.hive.utils.RegistrationCompleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ModelMapper modelMapper;

    private final String verificationUrl = "http://localhost:9090/auth";



    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {

        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).get().getUser());

    }

    @Override
    @Transactional
    public UserRegistrationResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        log.info("register user and create account");

        if (doesUserAlreadyExist(registrationRequestDto.getEmail())) {
            throw new CustomException("User already exist", HttpStatus.FORBIDDEN);
        }
        User newUser = saveNewUser(registrationRequestDto);
        // generateToken and Save to token repo, send email also
        eventPublisher.publishEvent(new RegistrationCompleteEvent(
                newUser,verificationUrl
        ));
        return modelMapper.map(newUser, UserRegistrationResponseDto.class);
    }


    private User saveNewUser(UserRegistrationRequestDto registrationRequestDto) {
        User newUser = new User();
        Role role = registrationRequestDto.getRole();
        Address address = registrationRequestDto.getAddress();
        addressRepository.save(address);

        BeanUtils.copyProperties(registrationRequestDto, newUser);
        newUser.addRole(role);
        newUser.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));


        return userRepository.save(newUser);
    }


    private boolean doesUserAlreadyExist(String email) {
        return userRepository.getUserByEmail(email).isPresent();
    }

    @Override
    public Boolean validateRegistrationToken(String token) {
        Boolean status = false;
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token)
                        .orElseThrow(() -> new CustomException("Token does not Exist : "+ token, HttpStatus.BAD_REQUEST));
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        // check if user is already verified
        if (user.getIsVerified()) {
            verificationTokenRepository.delete(verificationToken);
            throw new CustomException("User is already verified", HttpStatus.BAD_REQUEST);
        }
        // check if token is expired
        if((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0){
            throw new CustomException("Token has expired", HttpStatus.BAD_REQUEST);

        }
        // check if token is valid
        if (verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() > 0 ) {
            user.setIsVerified(true);
            userRepository.save(user);
            verificationTokenRepository.delete(verificationToken);
            status = true;
        }
        return status;
    }

    @Override
    public  String generateVerificationToken(User user) {
        log.info("inside generateVerificationToken, generating token for {}", user.getEmail());
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + 900000);
        verificationToken.setExpirationTime(expirationDate);

        log.info("Saving token to database {}", token);
        verificationTokenRepository.save(verificationToken);
        return token;
    }
    @Override
    public VerificationToken generateNewToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken)
                .orElseThrow(() -> new CustomException("Token does not Exist", HttpStatus.BAD_REQUEST));
        verificationToken.setToken(UUID.randomUUID().toString());
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + 900000);
        verificationToken.setExpirationTime(expirationDate);
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

}
