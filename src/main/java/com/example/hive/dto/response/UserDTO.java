package com.example.hive.dto.response;

import com.bctech.bookreviewproject.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String fullName;
    private Collection<Role> roles = new ArrayList<>();
}
