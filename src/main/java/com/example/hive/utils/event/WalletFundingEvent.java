package com.example.hive.utils.event;

import com.example.hive.entity.Task;
import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WalletFundingEvent extends ApplicationEvent {

    private Task task;

    public WalletFundingEvent(Object source, Task task) {
        super(source);
        this.task = task;
    }
}
