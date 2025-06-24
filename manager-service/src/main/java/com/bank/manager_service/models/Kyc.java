package com.bank.manager_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Kyc {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String aadharNo;
    private String panNo;
    private String image;
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
}
