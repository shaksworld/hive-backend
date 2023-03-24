package com.example.hive.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FetchAccountResponseDto {
    private long accountNumber;
    private BigDecimal accountBalance;
    private boolean isActivated;
}
