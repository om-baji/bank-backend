package com.bank.transaction.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        Map<String,String> map = new HashMap<>();

        map.put("message", "Transaction Service is Healthy!");
        map.put("success", "true");
        map.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(200)
                .body(map);
    }
}
