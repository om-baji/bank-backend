package com.example.bank.schemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTransactionSchema {
    private String from;

    private String accountNumber;

    private String to;

    private Double amount;
}
