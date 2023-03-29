package com.example.hive.service;

import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {

    boolean creditDoerWallet(UUID doerId, BigDecimal creditAmount, TransactionLog transactionLog);

}
