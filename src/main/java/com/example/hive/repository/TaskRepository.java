package com.example.hive.repository;
import com.example.hive.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface TaskRepository extends JpaRepository<Task, UUID> {
}
