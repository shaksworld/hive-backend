package com.example.hive.entity;
import com.example.hive.constant.Status;
import jakarta.persistence.*;
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

    private String taskDescription;

    @Column(name = "budget_rate")
    private BigDecimal budgetRate;

    @OneToOne
    private Address taskAddress;

    @OneToOne
    private Address deliverAddress;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "task_duration")
    private LocalDateTime taskDuration;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private User tasker;

    @ManyToOne
    private User doer;

}
