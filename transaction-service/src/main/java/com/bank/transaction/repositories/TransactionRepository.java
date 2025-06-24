package com.bank.transaction.repositories;

import com.bank.transaction.enums.TransactionStatus;
import com.bank.transaction.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Optional<Transaction> findById(String id);

    List<Transaction> findAllByStatus(TransactionStatus status);

    Page<Transaction> findAllByUserId(String userId, Pageable pageable);

    Boolean existsByFromAccount(String fromAccount);

    Optional<Transaction> findByFromAccount(String fromAccount);


}
