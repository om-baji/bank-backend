package com.example.bank.services;

import com.example.bank.config.MessageProducer;
import com.example.bank.models.Accounts;
import com.example.bank.models.TransactionDTO;
import com.example.bank.models.Transactions;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.schemas.TransactionSchema;
import com.example.bank.schemas.UserTransactionSchema;
import com.example.bank.util.AmountParams;
import com.example.bank.util.CurrencyManager;
import com.example.bank.util.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageProducer producer;

    @Autowired
    private Helpers helpers;

    @Transactional
    public List<TransactionDTO> getTransactionsByPage(String accountId,
                                                    Integer page, Integer size) throws Exception {
        Optional<Accounts> exists = accountRepository.findById(accountId);

        if (exists.isEmpty()) throw new Exception("Account not found!");

        Accounts account = exists.get();

        List<Transactions> transactions = new ArrayList<>();

        transactions.addAll(account.getSentTransactions());
        transactions.addAll(account.getReceivedTransactions());

        transactions.sort(Comparator.comparing(Transactions::getCreatedAt));

        // Add pagination logic if needed (page, size)

        List<TransactionDTO> finalTxns = new ArrayList<>();

        transactions.forEach(txn -> finalTxns.add(helpers.toDTO(txn)));

        return finalTxns;
    }

    public TransactionDTO getTransaction(String txnId) throws Exception {
        Optional<Transactions> transaction = repository.findById(txnId);

        if (transaction.isEmpty()) throw new Exception("Transaction not found!");

        return helpers.toDTO(transaction.get());
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionSchema schema) {
        Transactions txn = new Transactions();
        txn.setCreatedAt(new Date());
        Map<String,Object> objectMap = new HashMap<>();

        try {
            String username = helpers.getCurrentUsername();

            Optional<Accounts> fromOpt = accountRepository.findByAccountNumberAndUser_Username(schema.getFrom(), username);
            Optional<Accounts> toOpt = accountRepository.findByAccountNumber(schema.getTo());

            if (fromOpt.isEmpty() || toOpt.isEmpty()) {
                txn.setStatus("FAILED"); objectMap.put("status","FAILED");
                txn.setDescription("Account Not Found"); objectMap.put("description","Account Not Found");
                return helpers.toDTO(repository.save(txn));
            }

            Accounts fromAccount = fromOpt.get();
            Accounts toAccount = toOpt.get();

            txn.setFromAccount(fromAccount);
            txn.setToAccount(toAccount);
            txn.setCurrency(fromAccount.getCurrencyCode());
            txn.setAmount(schema.getAmount());

            if (!fromAccount.getCurrencyCode().equals(toAccount.getCurrencyCode())) {
                txn.setStatus("FAILED"); objectMap.put("status", "FAILED");
                txn.setDescription("Currency mismatch between accounts"); objectMap.put("description", "Currency mismatch between accounts");
                return helpers.toDTO(repository.save(txn));
            }

            long fromBalanceRaw = fromAccount.getBalanceRaw();
            long transferAmountRaw = CurrencyManager.toRawAmount(BigDecimal.valueOf(schema.getAmount()), fromAccount.getCurrencyCode());

            if (fromBalanceRaw < transferAmountRaw) {
                txn.setStatus("FAILED"); objectMap.put("status", "FAILED");
                txn.setDescription("Insufficient balance"); objectMap.put("description", "Insufficient balance");
                return helpers.toDTO(repository.save(txn));
            }

            fromAccount.setBalanceRaw(fromBalanceRaw - transferAmountRaw);
            toAccount.setBalanceRaw(toAccount.getBalanceRaw() + transferAmountRaw);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            txn.setStatus("SUCCESS"); objectMap.put("status", "SUCCESS");
            txn.setDescription("Transfer completed"); objectMap.put("description", "Transfer completed");

            objectMap.put("amount" , schema.getAmount());
            objectMap.put("fromAccount", fromAccount.getAccountNumber());
            objectMap.put("toAccount", toAccount.getAccountNumber());
            objectMap.put("username", helpers.getCurrentUsername());
            objectMap.put("transactionId", txn.getId());

        } catch (Exception e) {
            txn.setStatus("FAILED"); objectMap.put("status", "SUCCESS");
            txn.setDescription("Internal error: " + e.getMessage()); objectMap.put("description", "Internal error: " + e.getMessage());
        } finally {
            objectMap.put("eventType", "ACCOUNT_TRANSFER");
            producer.pushMessage("banking.transaction.events", objectMap);
            producer.pushMessage("bank.email.service", objectMap);
        }

        return helpers.toDTO(repository.save(txn));
    }

    public List<TransactionDTO> getTransactionByAccount(String id) throws Exception {

        Optional<Accounts> exists = accountRepository.findByAccountNumber(id);

        if(exists.isEmpty())
            throw new Exception("Account Not found!");

        Accounts account = exists.get();

        List<Transactions> transactions = repository.findByFromAccount_IdOrToAccount_Id(account.getId(),account.getId());

        List<TransactionDTO> list = new ArrayList<>();

        transactions.forEach(txn -> list.add(helpers.toDTO(txn)));

        return list;
    }

//    public TransactionDTO createUserTxn(UserTransactionSchema schema) {
//
//        TransactionDTO txn = TransactionDTO.builder()
//                .createdAt(new Date())
//                .status("PENDING")
//                .currency(null)
//                .amount(schema.getAmount())
//                .fromAccountNumber(schema.getAccountNumber())
//                .fromUser(schema.getFrom())
//                .toUser(schema.getTo())
//                .build();
//
//        try {
//            Optional<Accounts> fromOpt = accountRepository.findByAccountNumber(schema.getAccountNumber());
//
//        }
//    }
}
