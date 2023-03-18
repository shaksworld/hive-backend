package com.example.hive.dto.response;

import com.example.hive.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
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

    public ApiResponse(HttpStatus created, String taskCreatedSuccessfully, TaskResponseDto taskFound) {
        ApiResponse<TaskResponseDto> apiResponse = new ApiResponse<>();
        apiResponse.setData(taskFound);
        apiResponse.setStatusCode(HttpStatus.FOUND);
        apiResponse.setMessage("Tasks fetched");
    }


    public ApiResponse(TaskResponseDto taskResponseDto) {
    }
}

