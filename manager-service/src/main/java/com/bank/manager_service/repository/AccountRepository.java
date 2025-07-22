package com.bank.manager_service.repository;

import com.bank.manager_service.enums.AccountStatus;
import com.bank.manager_service.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {

    List<Account> findAllByStatus(AccountStatus status);
}
