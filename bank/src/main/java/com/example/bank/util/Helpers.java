package com.example.bank.util;

import com.example.bank.models.StatementBody;
import com.example.bank.models.TransactionDTO;
import com.example.bank.models.Transactions;
import com.example.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class Helpers {

    private String url = "";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate template;

    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    public String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    public ResponseEntity<?> successResponse(Object obj, String message) {
        Map<String,Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("data", obj);
        response.put("message" , message);

        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<?> successResponse(Object obj) {
        Map<String,Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("data", obj);
        response.put("message" , "");

        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<?> errorResponse(Exception e, String message) {
        Map<String,Object> response = new HashMap<>();

        response.put("status", "failed");
        response.put("error", e.getMessage());
        response.put("message" , message);

        return ResponseEntity.status(403).body(response);
    }

    public ResponseEntity<?> errorResponse(Exception e) {
        Map<String,Object> response = new HashMap<>();

        response.put("status", "failed");
        response.put("error", e.getMessage());
        response.put("message" , "Something went wrong!");

        return ResponseEntity.status(403).body(response);
    }

    public AmountParams getAmountParams(Double amount) {
        int decimals = BigDecimal.valueOf(amount).scale();
        Long raw = BigDecimal.valueOf(amount)
                .movePointRight(decimals)
                .longValue();

        return new AmountParams(raw, decimals);
    }

    public TransactionDTO toDTO(Transactions txn) {
        return TransactionDTO.builder()
                .id(txn.getId())
                .amount(txn.getAmount())
                .description(txn.getDescription())
                .status(txn.getStatus())
                .createdAt(txn.getCreatedAt())
                .currency(txn.getCurrency())
                .fromAccountNumber(
                        txn.getFromAccount() != null ? txn.getFromAccount().getAccountNumber() : null
                )
                .fromUser(
                        txn.getFromAccount() != null && txn.getFromAccount().getUser() != null
                                ? txn.getFromAccount().getUser().getUsername()
                                : null
                )
                .toAccountNumber(
                        txn.getToAccount() != null ? txn.getToAccount().getAccountNumber() : null
                )
                .toUser(
                        txn.getToAccount() != null && txn.getToAccount().getUser() != null
                                ? txn.getToAccount().getUser().getUsername()
                                : null
                )
                .build();
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    public ResponseEntity<String> getStatement(StatementBody body,Boolean isMonthly) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StatementBody> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = template.exchange(
                url + "?monthly=" + isMonthly,
                HttpMethod.POST,
                request,
                String.class
        );

        return response;
    }

    public void getStatement(StatementBody body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StatementBody> request = new HttpEntity<>(body, headers);

        template.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );
    }


}
