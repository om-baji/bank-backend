package com.example.bank.repository;

import com.example.bank.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,String> {

    List<Transactions> findByFromAccount_IdOrToAccount_Id(String fromId, String toId);
}
