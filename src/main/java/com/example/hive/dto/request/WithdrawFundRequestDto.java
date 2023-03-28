package com.example.hive.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawFundRequestDto {
    @NotNull(message = "amount cannot be null")
    private BigDecimal amount;
    private Long accountNumber;
}
