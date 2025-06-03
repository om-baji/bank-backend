package com.example.bank.services;

import com.example.bank.config.MessageProducer;
import com.example.bank.models.*;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.UserRepository;
import com.example.bank.util.CurrencyManager;
import com.example.bank.util.Helpers;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageProducer producer;

    @Autowired
    private Helpers helper;

    public List<AccountModel> fetchAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Accounts> accounts = repository.findByUser_Username(username);

        List<AccountModel> finalAccounts = new ArrayList<>();

        accounts.forEach(account -> {
            long rawBalance = account.getBalanceRaw();
            String currency = account.getCurrencyCode();
            BigDecimal amount = CurrencyManager.fromRawAmount(rawBalance, currency);
            finalAccounts.add(AccountModel
                    .builder()
                    .id(account.getId())
                    .accountNumber(account.getAccountNumber())
                    .status(account.getStatus())
                    .balance(amount.doubleValue())
                    .accountType(account.getAccountType())
                    .createdAt(account.getCreatedAt())
                    .build()
            );
        });

        return finalAccounts;
    }

    public AccountModel fetchAccount(String id) throws Exception {
        Optional<Accounts> exists = repository.findById(id);

        if (exists.isEmpty())
            throw new Exception("Account Not found!");

        Accounts account = exists.get();

        long rawBalance = account.getBalanceRaw();
        String currency = account.getCurrencyCode();
        BigDecimal amount = CurrencyManager.fromRawAmount(rawBalance, currency);

        return AccountModel
                .builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .balance(amount.doubleValue())
                .createdAt(account.getCreatedAt())
                .id(account.getId())
                .build();
    }

    public Double fetchAccountBalance(String id) throws Exception {
        Optional<Accounts> exists = repository.findById(id);

        if (exists.isEmpty()) throw new Exception("Account Not found!");

        Accounts account = exists.get();
        long rawBalance = account.getBalanceRaw();
        String currency = account.getCurrencyCode();

        BigDecimal amount = CurrencyManager.fromRawAmount(rawBalance, currency);
        return amount.doubleValue();
    }

    @Transactional
    public AccountModel saveAccount(AccountRequest accountBody) throws Exception {
        Optional<Users> exists = userRepository.findByUsername(accountBody.getUsername());

        if (exists.isEmpty()) throw new Exception("User Not found!");

        Users user = exists.get();

        String currencyCode = accountBody.getCurrencyCode();

        CurrencyManager.getDecimals(currencyCode);

        long rawDeposit = CurrencyManager.toRawAmount(BigDecimal.valueOf(accountBody.getDeposit()), currencyCode);

        Accounts accounts = Accounts.builder()
                .user(user)
                .accountType(accountBody.getAccountType())
                .accountNumber(helper.generateUniqueAccountNumber())
                .status(accountBody.getStatus())
                .currencyCode(currencyCode)
                .balanceRaw(rawDeposit)
                .createdAt(new Date())
                .build();

        accounts = repository.save(accounts);
        Map<String,String> messageObj = new HashMap<>();

        messageObj.put("eventType", "ACCOUNT_CREATED");
        messageObj.put("account_no" , accounts.getAccountNumber().substring(0,4) + "XXXX XXXX XXXX");
        messageObj.put("timestamp", new Date().toString());
        messageObj.put("username" , helper.getCurrentUsername());

        producer.pushMessage("banking.account.events", messageObj);

        return AccountModel.builder()
                .accountType(accounts.getAccountType())
                .accountNumber(accounts.getAccountNumber())
                .status(accounts.getStatus())
                .balance(accountBody.getDeposit())
                .createdAt(accounts.getCreatedAt())
                .build();
    }
}