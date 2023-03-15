package com.example.hive.dto.response;

import com.bctech.bookreviewproject.exceptions.ErrorDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class APIResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
    private ErrorDetails error;
    private final LocalDateTime time = LocalDateTime.now();

}
