package com.example.hive.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class TransactionResponse<T> {
    private String status;
    private String message;
    private T data;

}

