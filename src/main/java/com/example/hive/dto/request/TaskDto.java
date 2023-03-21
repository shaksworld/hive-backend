package com.example.hive.dto.request;

import com.example.hive.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private UUID task_id;
    private String jobType;
    @Size(max = 250)
    private String taskDescription;
    private BigDecimal budgetRate;
    private String taskAddress;
    private String taskDeliveryAddress;
    private Integer estimatedTime;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String taskDuration;
    private Status status;
    private String tasker_id;
    private String doer_id;
}
