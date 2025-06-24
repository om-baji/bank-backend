package com.bank.transaction.models;

import com.bank.transaction.enums.TransactionStatus;
import com.bank.transaction.enums.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long amount;
    private String currencyCode;
    private String fromAccount;
    private String toAccount;
    private Date initiatedAt;
    private Date completedAt;
    private TransactionStatus status;

    private TransactionType Type;
    private String userId;
}
