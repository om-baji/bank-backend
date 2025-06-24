package com.bank.manager_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean isVerified;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Kyc kyc;
}

