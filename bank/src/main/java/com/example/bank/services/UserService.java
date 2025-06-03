package com.example.bank.services;

import com.example.bank.models.Users;
import com.example.bank.repository.UserRepository;
import com.example.bank.schemas.LoginSchema;
import com.example.bank.schemas.RegisterSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtService service;

    @Autowired
    private AuthenticationManager manager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users saveUser(RegisterSchema registerSchema) {
        Users user = Users
                .builder()
                .first_name(registerSchema.getFirstName())
                .last_name(registerSchema.getLastName())
                .createdAt(new Date())
                .username(registerSchema.getUsername())
                .password(encoder.encode(registerSchema.getPassword()))
                .build();

        return repository.save(user);
    }

    public String loginUser(LoginSchema loginSchema) {
        Authentication auth =
                manager.authenticate(new UsernamePasswordAuthenticationToken(loginSchema.getUsername(),loginSchema.getPassword()));

        if(auth.isAuthenticated()) return service.generateToken(loginSchema.getUsername());

        return null;
    }
}
