package com.bank.transaction.controllers;

import com.bank.transaction.schemas.TransactionSchema;
import com.bank.transaction.services.TransactionService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public ResponseEntity<?> initiateTransaction(
            @RequestBody TransactionSchema transactionSchema) throws Exception {
        return service.createTransaction(transactionSchema);
    }

    @GetMapping
    public ResponseEntity<?> userTransactions(
            @PathVariable(name = "page") int page,
            @PathVariable(name = "limit") int limit
    ) {
        return service.getTransactionsByUser(page,limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionsById(
            @PathParam(value = "id") String id) {
        return service.TransactionById(id);
    }
}
