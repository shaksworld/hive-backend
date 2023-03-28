package com.example.hive.service.implementation;

import com.example.hive.constant.TransactionStatus;
import com.example.hive.constant.TransactionType;
import com.example.hive.dto.Request.PayStackPaymentRequest;
import com.example.hive.dto.Request.TaskerPaymentRequest;
import com.example.hive.dto.response.PayStackResponse;
import com.example.hive.dto.response.VerifyTransactionResponse;
import com.example.hive.entity.Task;
import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;
import com.example.hive.exceptions.CustomException;
import com.example.hive.exceptions.ResourceNotFoundException;
import com.example.hive.repository.TaskRepository;
import com.example.hive.repository.TransactionLogRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.PayStackService;
import com.example.hive.service.PaymentService;
import com.example.hive.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PayStackService payStackService;
    private final TransactionLogRepository transactionLogRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final WalletService walletService;


    @Override
    public PayStackResponse makePaymentToDoer(TaskerPaymentRequest taskerPaymentRequest, Principal principal) throws Exception {

        // get logged-in user and check if they are a tasker
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // get task and check if it exists
        UUID taskId = UUID.fromString(taskerPaymentRequest.getTaskId());


        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        //check if the task set amount is equal to the amount sent by the user
//log.info("task.getBudgetRate() = " + task.getBudgetRate());
//log.info("taskerPaymentRequest.getAmount() = " + BigDecimal.valueOf(taskerPaymentRequest.getAmount()));
//     var amount =
//        if (!task.getBudgetRate().equals(BigDecimal.valueOf(taskerPaymentRequest.getAmount()))) {
//            throw new CustomException("Amount sent is not equal to the amount set by the tasker");
//        }

            var payStackPaymentRequest = PayStackPaymentRequest.builder()
                    .amount(taskerPaymentRequest.getAmount())
                    .email(user.getEmail())
                    .build();
           PayStackResponse payStackResponse = payStackService.initTransaction(principal, payStackPaymentRequest);

           // save to trasanction log to save the details of the payment
           saveToTransactionLog(payStackResponse, task, user, taskerPaymentRequest);
           return payStackResponse;

    }

    private void saveToTransactionLog(PayStackResponse payStackResponse, Task task, User user, TaskerPaymentRequest taskerPaymentRequest) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setAmount(BigDecimal.valueOf(taskerPaymentRequest.getAmount()));
        transactionLog.setPaystackReference(payStackResponse.getData().getReference());
        transactionLog.setTransactionType(TransactionType.DEPOSIT);
        transactionLog.setTransactionStatus(TransactionStatus.PENDING);
        transactionLog.setTaskerDepositor(user);
        transactionLog.setDoerWithdrawer(task.getDoer());
        transactionLog.setTask(task);
        transactionLogRepository.save(transactionLog);
    }

    @Override
    @Transactional
    public VerifyTransactionResponse verifyAndCompletePayment(String reference) throws IOException {
        VerifyTransactionResponse verifyTransactionResponse = null;
        try {
            verifyTransactionResponse = payStackService.verifyPayment(reference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var status = verifyTransactionResponse.getData().getStatus();

        if (status.equals("success")) {
            TransactionLog transactionLog = transactionLogRepository.findByPaystackReference(reference).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
            transactionLog.setTransactionStatus(TransactionStatus.SUCCESS);
            transactionLogRepository.save(transactionLog);

            if (!completeTransactionByCreditingDoer(transactionLog)) {
                throw new CustomException("Transaction failed");
            }

        } else {
            throw new CustomException("Transaction failed");
        }

        return verifyTransactionResponse;

    }

    private boolean completeTransactionByCreditingDoer(TransactionLog transactionLog){
        User doer = transactionLog.getDoerWithdrawer();
        BigDecimal creditAmount = transactionLog.getAmount();
     return  walletService.creditDoerWallet(doer,creditAmount,transactionLog);
    }

    }

