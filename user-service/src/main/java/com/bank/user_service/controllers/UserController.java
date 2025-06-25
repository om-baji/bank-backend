package com.bank.user_service.controllers;

import com.bank.user_service.schemas.ApiResponse;
import com.bank.user_service.schemas.LoginSchema;
import com.bank.user_service.schemas.RegisterSchema;
import com.bank.user_service.services.JwtService;
import com.bank.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterSchema schema) {
        return service.saveUser(schema);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginSchema schema) {

        String token = service.loginUser(schema);

        if(token == null) return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse(false,"Incorrect credentials!"));

        Map<String,String> obj = new HashMap<>();

        obj.put("success" , "true");
        obj.put("token", token);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(obj);
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile() {
        return service.getProfile();
    }
}
