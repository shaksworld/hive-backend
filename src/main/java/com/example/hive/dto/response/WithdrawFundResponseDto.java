package com.example.hive.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WithdrawFundResponseDto {
    private Long accountNumber;
    private String bankName;
    private boolean isSuccessful;
}
