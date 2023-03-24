package com.example.hive.service;

import com.example.hive.dto.Request.WithdrawFundRequestDto;
import com.example.hive.dto.response.FetchAccountResponseDto;
import com.example.hive.dto.response.WithdrawFundResponseDto;
import com.example.hive.security.config.CustomUserDetails;

public interface WalletService {
    WithdrawFundResponseDto withdrawFunds(WithdrawFundRequestDto withdrawFundRequestDto, CustomUserDetails customUserDetails);


}
