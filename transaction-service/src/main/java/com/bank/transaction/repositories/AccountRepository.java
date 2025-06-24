package com.bank.transaction.repositories;

import com.bank.transaction.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {

    Optional<Account> findByUserId(String id);

    Optional<Account> findByAccountNumber(String account);
}
