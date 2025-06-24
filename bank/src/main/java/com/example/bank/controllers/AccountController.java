package com.example.bank.controllers;

import com.example.bank.models.AccountModel;
import com.example.bank.models.AccountRequest;
import com.example.bank.services.AccountService;
import com.example.bank.util.Helpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private Helpers helpers;

    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        List<AccountModel> accounts = accountService.fetchAccounts();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Accounts fetched successfully");
        response.put("data", accounts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable String id) {
        try {
            AccountModel account = accountService.fetchAccount(id);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Account fetched successfully");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return helpers.errorResponse(e);
        }
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getAccountBalance(@PathVariable String id) {
        try {
            Double balance = accountService.fetchAccountBalance(id);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Balance fetched successfully");
            response.put("data", balance);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return helpers.errorResponse(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest accountBody) {
        try {
            AccountModel newAccount = accountService.saveAccount(accountBody);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Account created successfully");
            response.put("data", newAccount);

            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return helpers.errorResponse(e);
        }
    }
}