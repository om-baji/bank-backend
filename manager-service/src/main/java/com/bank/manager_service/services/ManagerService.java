package com.bank.manager_service.services;

import com.bank.manager_service.enums.AccountStatus;
import com.bank.manager_service.helpers.Helper;
import com.bank.manager_service.models.Account;
import com.bank.manager_service.models.Transaction;
import com.bank.manager_service.models.Users;
import com.bank.manager_service.repository.AccountRepository;
import com.bank.manager_service.repository.UserRepository;
import com.bank.manager_service.schemas.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManagerService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Helper helper;

    public ResponseEntity<?> pendingKYCusers() {

        List<Users> users = repository.findAllByIsVerified(false);

        return ResponseEntity.status(200).body(users);
    }

    public ResponseEntity<?> verifyUser(String id) {

        Optional<Users> exist = repository.findById(id);

        if(exist.isEmpty()) return ResponseEntity.status(404)
                .body(new ApiResponse(false,"User Not Found!"));

        Users user = exist.get();

        user.setIsVerified(true);

        repository.save(user);

        return ResponseEntity.status(200)
                .body(new ApiResponse(true,"User Verified!"));
    }

    public ResponseEntity<?> checkManager() {
        String username = helper.getCurrentUsername();

        Optional<Users> exist = repository.findByUsername(username);

        if(exist.isEmpty()) return ResponseEntity.status(404)
                .body(new ApiResponse(false,"User Not Found!"));

        Users manager = exist.get();

        Map<String,String> body = new HashMap<>();
        body.put("username", manager.getUsername());
        body.put("role", manager.getRole());
        body.put("email", manager.getEmail());
        body.put("message", "Verified manager!");

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(body);
    }

    public List<Transaction> getPendingTxns() {

        return new ArrayList<Transaction>();
    }

    public Transaction getPendingTxnsById(String id) {

        return new Transaction();
    }

    public ResponseEntity<?> fetchAccounts() {

        List<Account> list = accountRepository.findAllByStatus(AccountStatus.UNAUTHORISED);

        return ResponseEntity.status(200).body(list);
    }
}
