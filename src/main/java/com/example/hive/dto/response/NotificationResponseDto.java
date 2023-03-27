package com.example.hive.dto.response;

import com.example.hive.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class NotificationResponseDto {

    private String title;

    private String body;

    private LocalDateTime createdAt;

}
