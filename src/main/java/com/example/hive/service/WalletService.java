package com.example.hive.service;

import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;

import java.math.BigDecimal;

public interface WalletService {

    boolean creditDoerWallet(User user, BigDecimal creditAmount, TransactionLog transactionLog);

}
