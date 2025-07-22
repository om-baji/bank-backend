package com.bank.account_service.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostAccount {

    private String accountType;
    private String deposit;
    private String currency;
}
