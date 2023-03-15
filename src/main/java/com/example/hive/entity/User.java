package com.example.hive.entity;
import com.example.hive.constant.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AuditEntity {
    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    @Column(name = "valid_id")
    private String validId;

    @OneToOne
    private Address address;

    private String password;

    private Boolean isVerified;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(targetEntity = Task.class)
    private List<Task> task;

    public void addRole(Role role) {
        this.role.getRole();
    }

    public Set<Role> getRoles() {
        return Collections.singleton(role);
    }

}
