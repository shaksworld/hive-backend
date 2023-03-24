package com.example.hive.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bank_id;
    private String bankName;
    private Long accountNumber;

}
