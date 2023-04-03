package com.example.hive.controller;


import com.example.hive.dto.request.BankTransferDto;
import com.example.hive.dto.request.TaskerPaymentRequest;
import com.example.hive.dto.response.*;
import com.example.hive.security.config.CacheManager;
import com.example.hive.service.PayStackService;
import com.example.hive.service.PaymentService;
import com.example.hive.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
    private final PaymentService paymentService;

    private final PayStackService payStackService;
    private final CacheManager cacheManager;

    private final WalletService walletService;
    private final PayStackService payStackServic;
    final String DEFAULT_PROVIDER = "paystack";


    @PostMapping("/payment")
    @Operation(summary = "Make Payment to Doer via Paystack Gateway", responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)))})
    public ResponseEntity<AppResponse<PayStackResponse>> taskerInitiatesPayment(@RequestBody @Valid final TaskerPaymentRequest taskerPaymentRequest, Principal principal) throws Exception {
       // we need to make a call to the paystack api to make the payment and get the response
        PayStackResponse response =  paymentService.makePaymentToDoer(taskerPaymentRequest, principal);
        return ResponseEntity.ok(AppResponse.buildSuccessTxn(response));
    }

    @PostMapping("/verifyPayment")
    @Operation(summary = "Verify Payment Transaction", responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)))})
    public ResponseEntity<AppResponse<VerifyTransactionResponse>> verifyAndCompletePayment(@RequestParam String reference) throws Exception {
        // we need to check the status of the payment and complete the transaction
        log.info("verifying- for :: [{}]", reference );
        VerifyTransactionResponse response = paymentService.verifyAndCompletePayment(reference);
        return ResponseEntity.ok(AppResponse.buildSuccessTxn(response));
    }
    @GetMapping("/banks")
    public List<ListBanksResponse> listBanks(@RequestParam(name = "provider", defaultValue = DEFAULT_PROVIDER) String provider) {
        return payStackServic.fetchBanks(provider);
    }

    @PostMapping(path = "/transfer", consumes = "application/json", produces = "application/json")
    public Mono<TransactionResponse> transferFunds(@RequestBody BankTransferDto dto,
                                                   @RequestParam(name = "provider", defaultValue = DEFAULT_PROVIDER) String provider) throws InterruptedException {
        return payStackServic.transferFunds(dto, provider);
    }



}
