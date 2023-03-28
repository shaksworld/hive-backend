package com.example.hive.service;
import com.example.hive.dto.Request.BankTransferDto;
import com.example.hive.dto.Request.WithdrawFundRequestDto;
import com.example.hive.dto.response.TransferResponse;
import com.example.hive.dto.response.WithdrawFundResponseDto;
import com.example.hive.security.config.CustomUserDetails;
import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;

import java.math.BigDecimal;
import java.security.Principal;

public interface WalletService {


    boolean creditDoerWallet(User user, BigDecimal creditAmount, TransactionLog transactionLog);

    TransferResponse makeWithdrawalFromWallet(BankTransferDto bankTransferDto, Principal principal);

    String verifyTransaction(String reference);
}
