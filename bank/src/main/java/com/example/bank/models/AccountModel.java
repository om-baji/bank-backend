package com.example.bank.models;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountModel {

    private String id;

    private String accountNumber;

    private double balance;

    private String accountType;

    private String status;

    private Date createdAt;
}
