package com.bank.manager_service.repository;

import com.bank.manager_service.models.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycRepository extends JpaRepository<Kyc, String> {

    Optional<Kyc> findByUser_Username(String username);

    Optional<Kyc> findByUser_Email(String email);
}
