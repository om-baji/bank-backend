package com.bank.transaction.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private int code;
    private String message;

    public ResponseEntity<?> ErrorResponse(int code,String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", HttpStatus.CONFLICT);
        map.put("code", code);
        map.put("message", message);

        return ResponseEntity.status(code).body(map);
    }
}
