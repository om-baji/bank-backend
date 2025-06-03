package com.example.bank.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.nio.DoubleBuffer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    private String username;

    private String accountType;

    private String status;

    private Long deposit;

    private String currencyCode;
}
