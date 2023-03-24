package com.example.hive.dto.request;



import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaskerPaymentRequest {

    private String taskId;

    @Digits(integer = 9, fraction = 0)
    private double amount;
}
