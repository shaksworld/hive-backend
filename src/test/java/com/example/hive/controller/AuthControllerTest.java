package com.example.hive.controller;

import com.example.hive.constant.Role;
import com.example.hive.dto.request.LogInRequest;
import com.example.hive.dto.request.UserRegistrationRequestDto;
import com.example.hive.entity.Address;
import com.example.hive.utils.validations.PhoneNumber;
import com.example.hive.utils.validations.ValidPassword;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void setUpBeforeEach() throws Exception {

        UserRegistrationRequestDto userRequest = getUserRequest();
                mockMvc.perform(post("/auth/register")
                .content(mapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON));


    }

//    @Test
//    void shouldCreateUserSuccessfully() throws Exception {
//        UserRegistrationRequestDto userRequest = getUserRequest();
//
//        mockMvc.perform(post("/auth/register")
//                .content(mapper.writeValueAsString(userRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }

    @Test
    void shouldLoginUser() throws Exception {
        LogInRequest request = loginUserRequest();
        mockMvc.perform(post("/auth/login")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public UserRegistrationRequestDto getUserRequest() {
        return UserRegistrationRequestDto.builder()
                .fullName("hive")
                .email("hive.supporter@gmail.com")
                .password("Ti@jr4rf")
                .confirmPassword("Ti@jr4rf")
                .phoneNumber("09123453245")
                .role(Role.TASKER)
                .validId("NIN")
                .build();
    }

    public LogInRequest loginUserRequest() {
        return LogInRequest.builder()
                .email("hive.supporter@gmail.com")
                .password("Ti@jr4rf")
                .build();
    }
}