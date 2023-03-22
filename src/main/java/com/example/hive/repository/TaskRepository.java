package com.example.hive.repository;
import com.example.hive.entity.Task;
import com.example.hive.entity.User;
import com.example.hive.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByTaskDescription(String taskDescription);

    List<Task> findAll();

    Optional<Task> findByJobTypeAndTaskDescriptionAndBudgetRateAndTaskAddressAndTaskDeliveryAddressAndEstimatedTimeAndStatus(String jobType, String taskDescription, BigDecimal budgetRate, String taskAddress, String taskDeliveryAddress, Integer estimatedTime, Status status);

    @Query("SELECT t FROM Task t WHERE t.doer = :doer AND t.status = 'ONGOING'")
    List<Task> findOngoingTasksByDoer(@Param("doer") User doer);

    @Query("SELECT t FROM Task t WHERE t.doer = :doer AND t.status = 'COMPLETED'")
    List<Task> findCompletedTasksByDoer(@Param("doer") User doer);
}

