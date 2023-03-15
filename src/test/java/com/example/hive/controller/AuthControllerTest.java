package com.example.hive.controller;

import com.example.hive.TestContainers;
import com.example.hive.constant.enums.Role;
import com.example.hive.dto.Request.UserRegistrationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        UserRegistrationRequestDto userRequest = getUserRequest();

        mockMvc.perform(post("/auth/register")
                        .content(mapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
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
}
