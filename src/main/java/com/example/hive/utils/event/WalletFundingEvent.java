package com.example.hive.utils.event;

import com.example.hive.entity.Task;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WalletFundingEvent extends ApplicationEvent {

    private final Task task;

    public WalletFundingEvent(Object source, Task task) {
        super(source);
        this.task = task;
    }
}
