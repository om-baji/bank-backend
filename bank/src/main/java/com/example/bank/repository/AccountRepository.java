package com.example.bank.repository;

import com.example.bank.models.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OpaqueTokenDsl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Accounts,String> {

    List<Accounts> findByUser_Username(String username);

    Boolean existsByUser_Username(String username);

    boolean existsByAccountNumber(String accountNumber);

    Optional<Accounts> findByAccountNumber(String num);

    Optional<Accounts> findByAccountNumberAndUser_Username(String acc,String username);
}
