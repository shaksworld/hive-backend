package com.example.hive.dto.response;

import com.example.hive.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class NotificationResponseDto {

    private String userId;

    private String title;

    private String body;

    private LocalDateTime createdAt;

    private String elapsedTime;

}
