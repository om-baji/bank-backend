package com.bank.account_service.dto;

import com.bank.account_service.enums.AccountStatus;
import com.bank.account_service.enums.AccountType;
import com.bank.account_service.models.Account;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class AccountDTO {

    private String id;
    private String accountNumber;
    private AccountType type;
    private Long balance;
    private String currency;
    private String userId;
    private AccountStatus status;
    private Date createdAt;

    public AccountDTO mapDTO(Account account) {
        return AccountDTO
                .builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .currency(account.getCurrency())
                .type(account.getType())
                .status(account.getStatus())
                .userId(account.getUserId())
                .build();
    }
}
