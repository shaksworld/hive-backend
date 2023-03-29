package com.example.hive.service;

import com.example.hive.dto.request.PayStackPaymentRequest;
import com.example.hive.dto.response.PayStackResponse;
import com.example.hive.dto.response.VerifyTransactionResponse;

import java.io.IOException;
import java.security.Principal;

public interface PayStackService {

    PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception;

    VerifyTransactionResponse verifyPayment(String reference) throws IOException;
}
