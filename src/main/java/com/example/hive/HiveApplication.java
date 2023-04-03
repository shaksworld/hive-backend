package com.example.hive;

import com.example.hive.entity.User;
import com.example.hive.entity.Wallet;
import com.example.hive.enums.Role;
import com.example.hive.repository.UserRepository;
import com.example.hive.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
public class HiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiveApplication.class, args);
	}

}
