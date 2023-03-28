package com.example.hive.repository;

import com.example.hive.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
    Optional<TransactionLog> findByPaystackReference(String reference);
}
