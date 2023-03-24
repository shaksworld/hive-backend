package com.example.hive.service.implementation;

import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;
import com.example.hive.entity.Wallet;
import com.example.hive.enums.Role;
import com.example.hive.exceptions.CustomException;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.WalletService;
import com.example.hive.utils.SuccessfulCreditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
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




}
