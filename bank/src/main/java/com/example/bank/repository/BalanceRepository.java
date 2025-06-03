package com.example.bank.repository;

import com.example.bank.models.Balances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balances,String> {
}
