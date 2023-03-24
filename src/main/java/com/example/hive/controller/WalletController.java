package com.example.hive.controller;

import com.example.hive.dto.Request.WithdrawFundRequestDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.WithdrawFundResponseDto;
import com.example.hive.security.config.CustomUserDetails;
import com.example.hive.service.WalletService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;
    @PostMapping("/withdraw")
    public ResponseEntity<AppResponse<WithdrawFundResponseDto>> withdrawFunds(@RequestBody @Valid final WithdrawFundRequestDto withdrawFundRequestDto, CustomUserDetails userDetails) {
        log.info("controller withdrawFunds- for :: [{}]", withdrawFundRequestDto.getAmount());
        WithdrawFundResponseDto response = walletService.withdrawFunds(withdrawFundRequestDto, userDetails);
        return ResponseEntity.ok(AppResponse.buildSuccessTxn(response));
    }
    }
