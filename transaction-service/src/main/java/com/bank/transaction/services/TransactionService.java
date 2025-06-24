package com.bank.transaction.services;

import com.bank.transaction.components.Producer;
import com.bank.transaction.enums.TransactionStatus;
import com.bank.transaction.helpers.Helper;
import com.bank.transaction.models.Account;
import com.bank.transaction.models.Transaction;
import com.bank.transaction.repositories.AccountRepository;
import com.bank.transaction.repositories.TransactionRepository;
import com.bank.transaction.schemas.ApiResponse;
import com.bank.transaction.schemas.TransactionSchema;
import com.sun.jdi.request.StepRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.apache.kafka.common.requests.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.rmi.server.ExportException;
import java.util.*;

@Service
@Slf4j
public class TransactionService {

    private final static String TRANSACTION_TOPIC = "bank.transaction.service";
    private final static String CONFLICTED_TOPIC = "bank.transaction.conflict";
    private final static String NOTIFICATION_TOPIC = "bank.notification.service";
    private final static String ROLLBACK_TOPIC = "bank.rollback.service";

    @Autowired
    private Helper helper;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Producer producer;

    List<Transaction> getAllTransactions(String username) {
        return new ArrayList<>();
    }

    @Transactional
    public ResponseEntity<?> createTransaction(TransactionSchema transactionSchema) throws Exception {

        String userId = helper.getCurrentId();

        Account fromAccount = accountRepository.findByUserId(userId)
                .orElseThrow();

        Account toAccount = accountRepository.findByAccountNumber(transactionSchema.getTo())
                .orElseThrow();

        if (!fromAccount.getAccountNumber().equals(transactionSchema.getFrom()) ||
                !fromAccount.getCurrencyCode().equals(transactionSchema.getCurrency()) ||
                !toAccount.getCurrencyCode().equals(transactionSchema.getCurrency())) {
            producer.messageProducer(CONFLICTED_TOPIC, new HashMap<>());
            producer.messageProducer(NOTIFICATION_TOPIC,new HashMap<>());
            return ResponseEntity.status(411).body("CONFLICTED ACCOUNTS!");
        }

        producer.messageProducer(TRANSACTION_TOPIC, new HashMap<>());

        Transaction transaction = Transaction
                .builder()
                .status(TransactionStatus.PENDING)
                .fromAccount(fromAccount.getAccountNumber())
                .toAccount(toAccount.getAccountNumber())
                .currencyCode(toAccount.getCurrencyCode())
                .build();

        if(fromAccount.getBalance() < transactionSchema.getAmount()) {
            producer.messageProducer(CONFLICTED_TOPIC, new HashMap<>());
            transaction.setStatus(TransactionStatus.FAILED);
            repository.save(transaction);
            producer.messageProducer(NOTIFICATION_TOPIC,transaction);
            return ResponseEntity.status(411).body("INSUFFICIENT BALANCE");
        }

        try {
            toAccount.setBalance(fromAccount.getBalance() - transactionSchema.getAmount());
            toAccount.setBalance(toAccount.getBalance() + fromAccount.getBalance());
        } catch (Exception ex) {
            log.error("Transaction failed! " + ex.getMessage());
            transaction.setStatus(TransactionStatus.WAITING);
            producer.messageProducer(ROLLBACK_TOPIC,new HashMap<>());
        }

        transaction.setStatus(TransactionStatus.COMPLETED);
        producer.messageProducer(TRANSACTION_TOPIC, new HashMap<>());
        producer.messageProducer(NOTIFICATION_TOPIC,transaction);

        return ResponseEntity.status(201)
                .body(new ApiResponse(201,"Transaction complete!"));
    }

    public ResponseEntity<?> getTransactionsByUser(int page, int limit) {
        String userId = helper.getCurrentId();

        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<Transaction> transactionsPage = repository.findAllByUserId(userId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", transactionsPage.getContent());
        response.put("currentPage", transactionsPage.getNumber());
        response.put("totalItems", transactionsPage.getTotalElements());
        response.put("totalPages", transactionsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> TransactionById(String id) {

        Optional<Transaction> txn = repository.findById(id);

        if(txn.isEmpty()) return ResponseEntity.status(404)
                    .body(new ApiResponse(404,"Not Found!"));

        Transaction transaction = txn.get();

        return ResponseEntity.status(200)
                .body(transaction);
    }
}