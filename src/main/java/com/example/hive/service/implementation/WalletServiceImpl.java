package com.example.hive.service.implementation;

import com.example.hive.constant.TransactionStatus;
import com.example.hive.constant.TransactionType;
import com.example.hive.dto.Request.BankTransferDto;
import com.example.hive.dto.Request.WithdrawFundRequestDto;
import com.example.hive.dto.response.PayStackTransferResponse;
import com.example.hive.dto.response.TransferResponse;
import com.example.hive.dto.response.WithdrawFundResponseDto;
import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;
import com.example.hive.entity.Wallet;
import com.example.hive.enums.Role;
import com.example.hive.exceptions.CustomException;
import com.example.hive.exceptions.InsufficientBalanceException;
import com.example.hive.exceptions.ResourceNotFoundException;
import com.example.hive.repository.TransactionLogRepository;
import com.example.hive.repository.TransactionRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.repository.WalletRepository;
import com.example.hive.security.config.CustomUserDetails;
import com.example.hive.service.PayStackService;
import com.example.hive.service.WalletService;
import com.example.hive.utils.SuccessfulCreditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final TransactionLogRepository transactionLogRepository;
    private final UserRepository userRepository;
    private final  WalletRepository walletRepository;

    private final PayStackService payStackService;
    private final ApplicationEventPublisher eventPublisher;




    @Override
    public boolean creditDoerWallet(User user, BigDecimal creditAmount, TransactionLog transactionLog){
        log.info("Crediting doer wallet{}", user.getEmail()) ;
        user = userRepository.findById(UUID.fromString("8a689b67-2585-4421-b2e6-dd4a04b0c286")).orElseThrow(() -> new CustomException("User not found"));
        boolean success = false;
        //check role of user
        if (!user.getRole().equals(Role.DOER)) {
            throw new CustomException("User is not a doer");
        }
        else {
            //check if user has a wallet
            Wallet wallet =  walletRepository.findByUser(user).orElseThrow(() -> new CustomException("User does not have a wallet"));
            if (wallet.getAccountBalance() == null) {wallet.setAccountBalance(creditAmount);}
            //credit wallet
            wallet.setAccountBalance(wallet.getAccountBalance().add(creditAmount));
            userRepository.save(user);
            eventPublisher.publishEvent(new SuccessfulCreditEvent(user, transactionLog));
            success = true;

        }
        return true;
    }

    @Override
    public TransferResponse makeWithdrawalFromWallet(BankTransferDto bankTransferDto, Principal principal) {
        //perform checks to verify user role
        User doer = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("USer not found"));

        // check account to vallidte wallet balance

        Wallet wallet =   walletRepository.findByUser(doer).orElseThrow();
         BigDecimal walletBalance = doer.getWallet().getAccountBalance();

        validateWalletBalance(bankTransferDto.getAmount(), walletBalance);


        // make transfer and witdraw from account, also save the trasaction

        TransferResponse transferResponse ;

        try{
            transferResponse = payStackService.transferFunds(bankTransferDto,doer);

            wallet.setAccountBalance(walletBalance.subtract(bankTransferDto.getAmount()));

        }
        catch (Exception e){
            throw new CustomException("Something wen wrong");
        }
        return transferResponse;
    }

    @Override
    public String verifyTransaction(String reference) {

        return payStackService.getTransactionStatusValue(reference).get();
    }


    private static void validateWalletBalance(BigDecimal withdrawal, BigDecimal walletBalance) {
        int difference = withdrawal.compareTo(walletBalance);

        if (difference < 0){
            throw new InsufficientBalanceException("GO and work and earn money jorr");
        }
    }


}





