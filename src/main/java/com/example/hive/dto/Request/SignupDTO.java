package com.example.hive.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Username is required")
    private String fullName;

    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
