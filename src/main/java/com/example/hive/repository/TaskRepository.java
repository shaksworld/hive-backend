package com.example.hive.repository;
import com.example.hive.entity.Task;
import com.example.hive.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByTaskDescription(String taskDescription);

    List<Task> findAll();

    @Query(value = "SELECT * from tasks t WHERE t.job_type LIKE ?1 or t.task_address LIKE ?1 or " +
            "t.task_delivery_address LIKE ?1 or t.task_description LIKE ?1" , nativeQuery = true)
    Optional<List<Task>> searchTasksBy(@RequestParam("query") String text);


    Optional<Task> findByJobTypeAndTaskDescriptionAndBudgetRateAndTaskAddressAndTaskDeliveryAddressAndEstimatedTimeAndStatus(String jobType, String taskDescription, BigDecimal budgetRate, String taskAddress, String taskDeliveryAddress, Integer estimatedTime, Status status);
}

