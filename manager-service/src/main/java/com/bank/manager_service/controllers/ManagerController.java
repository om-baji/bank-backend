package com.bank.manager_service.controllers;

import com.bank.manager_service.models.Transaction;
import com.bank.manager_service.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/pending-kyc")
    public ResponseEntity<?> pendingKYC() {
        return managerService.pendingKYCusers();
    }

    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verifyUser(@PathVariable String id) {
        return managerService.verifyUser(id);
    }

    @GetMapping("/pending-approval")
    public List<Transaction> getAllApprovals() {
        return managerService.getPendingTxns();
    }

    @GetMapping("/pending-approval/{id}")
    public Transaction getApprovalById(@PathVariable String id) {
        return managerService.getPendingTxnsById(id);
    }

    @GetMapping("/check")
    public ResponseEntity<?> verifyManager() {
        return managerService.checkManager();
    }
}
