package com.bank.transaction.schemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionSchema {

    private String from;
    private String to;
    private Long amount;
    private String currency;
}
