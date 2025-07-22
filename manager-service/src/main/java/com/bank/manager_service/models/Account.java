package com.bank.manager_service.models;

import com.bank.manager_service.enums.AccountStatus;
import com.bank.manager_service.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String accountNumber;
    private AccountType type;
    private Long balance;
    private String currency;
    private String userId;
    private AccountStatus status;
    private Date createdAt;
}
