package com.example.hive.entity;
import com.example.hive.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "transactions")
public class TransactionLog extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transaction_id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "sender", length = 50)
    private String sender;

    @Column(name = "sender_account_number")
    private long senderAccountNumber;

    @Column(name = "receiver", length = 50)
    private String receiver;

    @Column(name = "receiver_account_number")
    private long receiverAccountNumber;


}
