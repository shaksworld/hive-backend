package com.example.hive.controller;


import com.example.hive.dto.request.TaskerPaymentRequest;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.PayStackResponse;
import com.example.hive.dto.response.VerifyTransactionResponse;
import com.example.hive.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final PaymentService transactionService;

    @PostMapping("/payment")
    public ResponseEntity<AppResponse<PayStackResponse>> taskerInitiatesPayment(@RequestBody @Valid final TaskerPaymentRequest taskerPaymentRequest, Principal principal) throws Exception {
       // we need to make a call to the paystack api to make the payment and get the response
        PayStackResponse response =  transactionService.makePaymentToDoer(taskerPaymentRequest, principal);
        return ResponseEntity.ok(AppResponse.buildSuccessTxn(response));
    }

    @PostMapping("/verifyPayment")
    public ResponseEntity<AppResponse<VerifyTransactionResponse>> verifyAndCompletePayment(@RequestParam String reference) throws IOException {
        // we need to check the status of the payment and complete the transaction

        log.info("verifying- for :: [{}]", reference );
        VerifyTransactionResponse response = transactionService.verifyAndCompletePayment(reference);
        return ResponseEntity.ok(AppResponse.buildSuccessTxn(response));
    }

    @PostMapping("/demo")
    public ResponseEntity<AppResponse<PayStackResponse>> credit(@RequestBody @Valid final TaskerPaymentRequest taskerPaymentRequest, Principal principal) throws Exception {
        // we need to make a call to the paystack api to make the payment and get the response
        PayStackResponse response =  transactionService.makePaymentToDoer(taskerPaymentRequest, principal);
        return ResponseEntity.ok(AppResponse.buildSuccessTxn(response));
    }

}
