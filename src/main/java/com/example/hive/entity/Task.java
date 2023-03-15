package com.example.hive.entity;
import com.example.hive.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@Entity
public class Task extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String task_id;
    @Column(name = "job_type")
    private String jobType;
    @Size(max = 250)
    private String taskDescription;
    @Column(name = "budget_rate(N)")
    private BigDecimal budgetRate;
//    @OneToOne
//    private Address taskAddress;
    private String taskAddress;
//    @OneToOne
//    private Address deliverAddress;
    private String taskDeliveryAddress;
    @Column(name = "estimated_time")
    private Integer estimatedTime;
    @Column(name = "task_duration(hrs)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalDateTime taskDuration;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User tasker;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User doer;
}
