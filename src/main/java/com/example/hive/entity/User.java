package com.example.hive.entity;
import com.example.hive.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String user_id;
    @Column(name = "full_name")
    private String fullName;
    @Column(unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "valid_id")
    private String validId;
    @OneToOne
    private Address address;
    private String password;
    private Boolean isVerified;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany
    private List<Task> task;
}
