package com.example.bank.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionModel {

    private String id;

    private Double amount;

    private String description;

    private String status;

    private Date createdAt;

    private String fromAccount;

    private String toAccount;
}
