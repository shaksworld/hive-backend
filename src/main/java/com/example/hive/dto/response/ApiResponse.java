package com.example.hive.dto.response;

import com.example.hive.entity.Task;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private HttpStatus statusCode;
    private String message;
    private T data;
    private LocalDateTime time = LocalDateTime.now();

    public ApiResponse(HttpStatus statusCode, String message, T data, LocalDateTime time) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.time = time;
    }

    public ApiResponse(HttpStatus created, String taskCreatedSuccessfully, Task savedTask) {
    }
}

