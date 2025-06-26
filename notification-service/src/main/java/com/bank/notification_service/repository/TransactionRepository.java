package com.bank.notification_service.repository;

import com.bank.notification_service.models.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<String, Transaction> {

    Optional<Transaction> findById(String id);

    List<Transaction> findAllUserId(String id);
}
