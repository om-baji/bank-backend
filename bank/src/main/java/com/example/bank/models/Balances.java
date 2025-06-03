package com.example.bank.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Balances {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long raw;

    private Integer decimals;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Accounts account;
}
