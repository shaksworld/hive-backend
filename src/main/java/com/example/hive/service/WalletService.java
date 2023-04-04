package com.example.hive.service;

import com.example.hive.entity.User;

import java.math.BigDecimal;

public interface WalletService {

    boolean creditDoerWallet(User doer, BigDecimal creditAmount);

    void withdrawFromWalletBalance(User user, BigDecimal amount);

}
