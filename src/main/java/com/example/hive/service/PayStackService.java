package com.example.hive.service;

import com.example.hive.dto.request.BankTransferDto;
import com.example.hive.dto.request.PayStackPaymentRequest;
import com.example.hive.dto.request.ValidateAccountDto;
import com.example.hive.dto.response.*;
import com.example.hive.entity.User;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface PayStackService {

    PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception;

    VerifyTransactionResponse verifyPayment(String reference) throws IOException;

    List<ListBanksResponse> fetchBanks(String provider);

    Mono<TransactionResponse> transferFunds(BankTransferDto dto, String provider) throws InterruptedException;


}
