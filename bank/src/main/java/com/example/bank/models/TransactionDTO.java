package com.example.bank.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    private String id;
    private Double amount;
    private String description;
    private String status;
    private Date createdAt;
    private String currency;

    private String fromAccountNumber;
    private String fromUser;

    private String toAccountNumber;
    private String toUser;
}

