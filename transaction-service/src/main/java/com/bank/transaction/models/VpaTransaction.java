package com.bank.transaction.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VpaTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long amount;
    private String currencyCode;
    private String from;
    private String to;
    private String username;
    private Date createdAt;
}
