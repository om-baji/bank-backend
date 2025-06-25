package com.bank.user_service.controllers;

import com.bank.user_service.schemas.RegisterSchema;
import com.bank.user_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Operations", description = "Admin-level APIs for user management")
public class AdminController {

    private final String adminKey;

    public AdminController(@Value("${admin.secret}") String adminKey) {
        this.adminKey = adminKey;
    }

    @Autowired
    private UserService service;

    @Operation(
            summary = "Create Admin User",
            description = "Creates a new user with admin privileges. Requires a valid admin key.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Admin user created successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid admin key"),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(
            @Parameter(description = "Admin authentication key", required = true)
            @RequestHeader(value = "X-Admin-Key", required = false) String incomingKey,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Admin registration data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterSchema.class))
            )
            @RequestBody RegisterSchema schema) {

        if (incomingKey == null || !incomingKey.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Admin Key");
        }

        return service.saveAdmin(schema);
    }

    @Operation(
            summary = "Create Manager User",
            description = "Creates a new user with manager privileges. Requires a valid admin key.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Manager user created successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid admin key"),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/create-manager")
    public ResponseEntity<?> createManager(
            @Parameter(description = "Admin authentication key", required = true)
            @RequestHeader(value = "X-Admin-Key", required = false) String incomingKey,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Manager registration data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterSchema.class))
            )
            @RequestBody RegisterSchema schema) {

        if (incomingKey == null || !incomingKey.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Admin Key");
        }

        return service.saveManager(schema);
    }
}
