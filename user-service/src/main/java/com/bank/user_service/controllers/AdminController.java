package com.bank.user_service.controllers;

import com.bank.user_service.schemas.RegisterSchema;
import com.bank.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private String adminKey;

    public AdminController(@Value("${admin.secret}") String adminKey) {
        this.adminKey = adminKey;
    }

    @Autowired
    private UserService service;

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(
            @RequestHeader(value = "X-Admin-Key", required = false) String incomingKey,
            @RequestBody RegisterSchema schema) {

        if (incomingKey == null || !incomingKey.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Admin Key");
        }

        return service.saveAdmin(schema);
    }

    @PostMapping("/create-manager")
    public ResponseEntity<?> createManager(
            @RequestHeader(value = "X-Admin-Key", required = false) String incomingKey,
            @RequestBody RegisterSchema schema) {

        if (incomingKey == null || !incomingKey.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Admin Key");
        }

        return service.saveManager(schema);
    }
}
