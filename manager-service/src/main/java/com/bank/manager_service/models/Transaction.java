package com.bank.manager_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    private String id;
    private Long amount;
    private String currencyCode;
    private String fromAccount;
    private String toAccount;
    private Date initiatedAt;
}
