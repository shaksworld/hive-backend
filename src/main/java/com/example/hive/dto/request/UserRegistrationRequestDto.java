package com.example.hive.dto.request;

import com.example.hive.constant.Role;
import com.example.hive.entity.Address;
import com.example.hive.utils.validations.PhoneNumber;
import com.example.hive.utils.validations.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserRegistrationRequestDto {

    @NotBlank(message = "name cannot be empty")
    @Pattern(regexp = "[a-zA-Z]*", message = "name can only have letters")
    @Size(message = "fullName character length cannot be less than 2 and more than 100", min = 2, max = 200)
    private String fullName;


    @NotBlank(message = "email cannot be empty")
    @Email(message = "Must be a valid email!")
    private String email;

    @ValidPassword
    private String password;

    //Interface Annotation and Validator to create custom Annotation
    @ValidPassword
    private String confirmPassword;

    @PhoneNumber
    @NotBlank(message = "phoneNumber cannot be empty")
    @Size(message = "Phone number character length cannot be less than 10 and more than 16", min = 10, max = 16)
    private String phoneNumber;

//    @NotBlank(message = "role field cannot be empty")
    private Role role;

    @NotBlank(message = "validId field cannot be empty")
    private String validId;

//    @NotBlank(message = "address field cannot be null")
    private Address address;

}
