package com.example.hive.service;
import com.example.hive.dto.request.BankTransferDto;
import com.example.hive.dto.response.TransferResponse;
import com.example.hive.entity.TransactionLog;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

public interface WalletService {



    boolean creditDoerWallet(UUID doerId, BigDecimal creditAmount, TransactionLog transactionLog);

//    TransferResponse makeWithdrawalFromWallet(BankTransferDto bankTransferDto, Principal principal);
//
//    String verifyTransaction(String reference);
}
