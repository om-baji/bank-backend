package com.example.bank.controllers;

import com.example.bank.models.AccountModel;
import com.example.bank.models.AccountRequest;
import com.example.bank.services.AccountService;
import com.example.bank.util.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private Helpers helpers;

    @GetMapping
    public ResponseEntity<?> getAccounts() {
        List<AccountModel> accounts = accountService.fetchAccounts();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Accounts fetched successfully");
        response.put("data", accounts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable String id) {
        try {
            AccountModel account = accountService.fetchAccount(id);

            Map<String,Object> response = new HashMap<>();
            response.put("status","success");
            response.put("message", "Account fetched successfully");
            response.put("data",account);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return helpers.errorResponse(e);
        }
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String id) {
        try {
            Double balance = accountService.fetchAccountBalance(id);

            Map<String,Object> response = new HashMap<>();
            response.put("status","success");
            response.put("message", "Balance fetched successfully!");
            response.put("data",balance);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return helpers.errorResponse(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> postAccount(@RequestBody AccountRequest accountBody) {
        try {
            AccountModel newAccount = accountService.saveAccount(accountBody);

            Map<String,Object> response = new HashMap<>();
            response.put("status","success");
            response.put("message", "Balance fetched successfully!");
            response.put("data",newAccount);

            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            return helpers.errorResponse(e);
        }
    }
}