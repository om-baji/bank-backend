package com.example.bank.controllers;

import com.example.bank.models.TransactionDTO;
import com.example.bank.models.Transactions;
import com.example.bank.schemas.TransactionSchema;
import com.example.bank.schemas.UserTransactionSchema;
import com.example.bank.services.TransactionService;
import com.example.bank.util.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private Helpers helpers;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions(@RequestParam(name = "id") String id,
              @RequestParam Integer page, @RequestParam Integer size)  {
        try {
            return service.getTransactionsByPage(id,page,size);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/transactions/account/{id}")
    public List<TransactionDTO> getTxnByAcc(@PathVariable String id){
        try {
            return service.getTransactionByAccount(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransactions(@RequestParam String txnId) {
        try {
            return service.getTransaction(txnId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/transaction/execute")
    public TransactionDTO postTransaction(@RequestBody TransactionSchema schema) {
        try {
            return service.createTransaction(schema);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}