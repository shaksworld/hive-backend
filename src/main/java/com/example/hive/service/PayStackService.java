package com.example.hive.service;

import com.example.hive.dto.Request.BankTransferDto;
import com.example.hive.dto.Request.PayStackPaymentRequest;
import com.example.hive.dto.Request.ValidateAccountDto;
import com.example.hive.dto.response.*;
import com.example.hive.entity.User;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface PayStackService {

    PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception;

    VerifyTransactionResponse verifyPayment(String reference) throws IOException;

    List<ListBanksResponse> fetchBanks();

    ValidateAccountResponse validateBankAccount(ValidateAccountDto validateAccountDto, String bankName);

    TransferResponse transferFunds(BankTransferDto bankTransferDto , User user);

    Optional<String> getTransactionStatusValue(String transactionReference);
}
