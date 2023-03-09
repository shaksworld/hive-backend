package com.example.hive.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditEntity implements Serializable {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
