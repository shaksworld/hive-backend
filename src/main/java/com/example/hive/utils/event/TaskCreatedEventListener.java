package com.example.hive.utils.event;

import com.example.hive.entity.Task;
import com.example.hive.entity.User;
import com.example.hive.service.EmailService;
import com.example.hive.utils.EmailTemplates;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Log4j2
public class TaskCreatedEventListener implements ApplicationListener<TaskCreatedEvent> {

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(TaskCreatedEvent event) {
        User user = event.getUser();
        Task task = event.getTask();

        try{
            emailService.sendEmail(EmailTemplates.taskCreationNotificationEmail(user, task, event.getApplicationUrl()));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
