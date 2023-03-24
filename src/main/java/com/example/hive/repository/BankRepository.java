package com.example.hive.repository;

import com.example.hive.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BankRepository extends JpaRepository<Bank, String> {

    Optional<Bank> findByBankNameAndAccountNumber(String bankName, Long accountNumber);
}
