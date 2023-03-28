package com.example.hive.service;

import com.example.hive.dto.Request.TaskerPaymentRequest;
import com.example.hive.dto.response.ListBanksResponse;
import com.example.hive.dto.response.PayStackResponse;

import com.example.hive.dto.response.VerifyTransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface PaymentService {


    PayStackResponse makePaymentToDoer(TaskerPaymentRequest taskerPaymentRequest, Principal principal) throws Exception;


    VerifyTransactionResponse verifyAndCompletePayment(String reference) throws IOException;


}
