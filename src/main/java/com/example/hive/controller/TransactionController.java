package com.example.hive.controller;


import com.example.hive.dto.Request.BankTransferDto;
import com.example.hive.dto.Request.TaskerPaymentRequest;
import com.example.hive.dto.Request.ValidateAccountDto;
import com.example.hive.dto.response.*;
import com.example.hive.security.config.CacheManager;
import com.example.hive.service.PayStackService;
import com.example.hive.service.PaymentService;
import com.example.hive.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final PaymentService transactionService;

    private final PayStackService payStackService;
    private final CacheManager cacheManager;

    private final WalletService walletService;
    private final String provider = "PayStack";

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
    @GetMapping("/banks")
    public ResponseEntity<List<ListBanksResponse>> fetchBanks() {
        List<ListBanksResponse> banks = cacheManager.getProviderBanks(provider);
        List<ListBanksResponse> response = banks.isEmpty() ? banks : payStackService.fetchBanks();
        return ResponseEntity.ok(response);
    }


    @PostMapping("/validate-bank-account")
    public ResponseEntity<ValidateAccountResponse> validateBankAccount(@Valid @RequestBody ValidateAccountDto validateAccountDto) {
        String bankName = cacheManager.getBankName(validateAccountDto.getCode());
        return ResponseEntity.ok(payStackService.validateBankAccount(validateAccountDto, bankName));
    }

    @PostMapping("/bank-transfer")
    public ResponseEntity<TransferResponse> makeWithdrawalFromWallet(@Valid @RequestBody BankTransferDto bankTransferDto, Principal principal) {
        return ResponseEntity.ok(walletService.makeWithdrawalFromWallet(bankTransferDto, principal));
    }

    @GetMapping("/transaction/{reference}")
    public ResponseEntity<String> transactionStatus(@PathVariable String reference) {

        return ResponseEntity.ok(walletService.verifyTransaction(reference));
    }

}
