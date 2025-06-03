package com.example.bank.controllers;

import com.example.bank.models.Users;
import com.example.bank.schemas.LoginSchema;
import com.example.bank.schemas.RegisterSchema;
import com.example.bank.services.CustomUserService;
import com.example.bank.services.JwtService;
import com.example.bank.services.RefreshTokenService;
import com.example.bank.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserService service;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody RegisterSchema registerSchema) {
        return service.saveUser(registerSchema);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginSchema loginSchema) {
        String token =  service.loginUser(loginSchema);

        return (token != null)? token : "Something went wrong!";
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token missing");
        }

        String refreshToken = header.substring(7);
        String username = refreshTokenService.extractUsername(refreshToken);
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (refreshTokenService.validateRefreshToken(refreshToken, userDetails)) {
            String accessToken = jwtService.generateToken(userDetails.getUsername());
            String newRefreshToken = refreshTokenService.generateRefreshToken(userDetails.getUsername());
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", newRefreshToken
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
;}