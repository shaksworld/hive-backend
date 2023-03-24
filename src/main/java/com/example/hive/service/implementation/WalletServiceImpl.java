package com.example.hive.service.implementation;

import com.example.hive.constant.ResponseStatus;
import com.example.hive.dto.Request.WithdrawFundRequestDto;
import com.example.hive.dto.response.FetchAccountResponseDto;
import com.example.hive.dto.response.WithdrawFundResponseDto;
import com.example.hive.entity.Bank;
import com.example.hive.entity.User;
import com.example.hive.entity.Wallet;
import com.example.hive.exceptions.InsufficientBalanceException;
import com.example.hive.exceptions.ResourceNotFoundException;
import com.example.hive.repository.BankRepository;
import com.example.hive.repository.TransactionRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.repository.WalletRepository;
import com.example.hive.security.HiveUserDetailsService;
import com.example.hive.security.config.CustomUserDetails;
import com.example.hive.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final UserRepository userRepository;
    private final  WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final BankRepository bankRepository;
    @Override
    public WithdrawFundResponseDto withdrawFunds(WithdrawFundRequestDto withdrawFundRequestDto, CustomUserDetails customUserDetails){
        User user = userRepository.findByEmail(customUserDetails.getUsername()).orElseThrow(
                ()-> new ResourceNotFoundException("user not found"));
        Wallet wallet = walletRepository.findByUser(user).orElseThrow(
                ()-> new ResourceNotFoundException("Wallet not found"));
        Bank bank = bankRepository.findByBankNameAndAccountNumber(withdrawFundRequestDto.getBank(), withdrawFundRequestDto.getAccountNumber()).orElseThrow(
                ()-> new ResourceNotFoundException("Bank or Account Number not found"));

        if(wallet.getAccountBalance().compareTo(withdrawFundRequestDto.getAmount()) >= 0){
            wallet.setAccountBalance(wallet.getAccountBalance().subtract(withdrawFundRequestDto.getAmount()));
            walletRepository.save(wallet);
            return new WithdrawFundResponseDto(bank.getAccountNumber(), user.getFullName(), true);
        }else{
            throw new InsufficientBalanceException("Account Balance is insufficient for this transaction");
        }

    }


}