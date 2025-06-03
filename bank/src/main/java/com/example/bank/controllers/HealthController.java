package com.example.bank.controllers;

import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> health = new HashMap<>();

        try {

            long accountCount = accountRepository.count();
            long userCount = userRepository != null
                    ? userRepository.count()
                    : -1;

            health.put("status", "UP");
            health.put("timestamp", Instant.now());
            health.put("accounts", accountCount);
            health.put("users", userCount);
            health.put("version", "v1.0.0");

            return ResponseEntity.ok(health);
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            health.put("timestamp", Instant.now());

            return ResponseEntity.status(503).body(health);
        }
    }
}
