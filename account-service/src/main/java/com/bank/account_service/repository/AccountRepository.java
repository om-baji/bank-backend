package com.bank.account_service.repository;

import com.bank.account_service.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {

    List<Account> findAllByUserId(String userId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
