package com.example.hive.utils;

import com.example.hive.repository.TaskRepository;
import com.example.hive.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class SuccessfulCreditEventListener implements ApplicationListener<SuccessfulCreditEvent> {
    private final EmailService emailService;
    private final TaskRepository taskRepository;

    @Override
    public void onApplicationEvent(SuccessfulCreditEvent event) {
       var doer = event.getUser();
       var taskTitle = event.getTransactionLog().getTask().getTaskDescription();
       var transactionLog = event.getTransactionLog();

        try {
            emailService.sendEmail(EmailTemplates.createSuccessfulCreditEmail(doer, taskTitle));
            emailService.sendEmail(EmailTemplates.createSuccessfulPaymentFromTaskerEmail(transactionLog.getTaskerDepositor(),taskTitle));

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

