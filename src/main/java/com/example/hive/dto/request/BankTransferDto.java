package com.example.hive.dto.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class BankTransferDto {
    @Positive
    BigDecimal amount = BigDecimal.ZERO;
    @NotEmpty
    String currencyCode = "NGN";
    @NotEmpty
    String narration;
    @NotEmpty
    String beneficiaryAccountNumber;
    String beneficiaryAccountName;
    @NotEmpty
    String beneficiaryBankCode;
    String transactionReference;
    int maxRetryAttempt = 0;
    String callBackUrl;

}
