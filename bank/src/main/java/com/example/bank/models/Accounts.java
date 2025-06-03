package com.example.bank.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String accountNumber;

    private long balanceRaw;

    private String currencyCode;

    private String accountType;

    private String status;

    private Date createdAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id" ,nullable = false)
    private Users user;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    private List<Transactions> sentTransactions;

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    private List<Transactions> receivedTransactions;
}