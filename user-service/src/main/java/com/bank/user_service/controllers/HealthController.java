package com.bank.user_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Health status for the User Service")
public class HealthController {

    @Operation(
            summary = "User Service Health Check",
            description = "Returns service status and uptime information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Service is up and running")
            }
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("message", "User Service is healthy");
        status.put("timestamp", Instant.now());

        return ResponseEntity.ok(status);
    }
}
