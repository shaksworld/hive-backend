package com.example.hive.repository;
import com.example.hive.entity.Task;
import com.example.hive.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByTaskDescription(String taskDescription);

    List<Task> findAll();

    @Query(value = "SELECT t from Task t WHERE " + "t.jobType LIKE CONCAT ('%', :query, '%')" + "OR t.taskDescription LIKE CONCAT ('%', :query, '%')" + "OR t.taskAddress LIKE CONCAT ('%', :query, '%')" + "OR t.taskAddress LIKE CONCAT ('%', :query, '%')")
    List<Task> searchTasksBy(String text);

    Optional<Task> findByJobTypeAndTaskDescriptionAndBudgetRateAndTaskAddressAndTaskDeliveryAddressAndEstimatedTimeAndStatus(String jobType, String taskDescription, BigDecimal budgetRate, String taskAddress, String taskDeliveryAddress, Integer estimatedTime, Status status);
}

