package com.example.hive.dto.response;

import com.example.hive.entity.Address;
import com.example.hive.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationResponseDto {

    private String fullName;
    private String email;
    private String phoneNumber;
    private Address address;
    private Boolean isVerified;
    private Role role;

}
