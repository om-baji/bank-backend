package com.example.bank.services;

import com.example.bank.models.StatementBody;
import com.example.bank.models.TransactionDTO;
import com.example.bank.models.Transactions;
import com.example.bank.models.Users;
import com.example.bank.repository.UserRepository;
import com.example.bank.schemas.LoginSchema;
import com.example.bank.schemas.RegisterResponse;
import com.example.bank.schemas.RegisterSchema;
import com.example.bank.util.Helpers;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtService service;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TransactionService txnService;

    @Autowired
    private Helpers helpers;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public RegisterResponse saveUser(RegisterSchema registerSchema) {
        Users user = Users
                .builder()
                .first_name(registerSchema.getFirstName())
                .last_name(registerSchema.getLastName())
                .createdAt(new Date())
                .username(registerSchema.getUsername())
                .password(encoder.encode(registerSchema.getPassword()))
                .build();
        logger.info("User is created!");
        return RegisterResponse.fromUserDTO(repository.save(user));
    }

    public String loginUser(LoginSchema loginSchema) {
        Authentication auth =
                manager.authenticate(new UsernamePasswordAuthenticationToken(loginSchema.getUsername(),loginSchema.getPassword()));

        if(auth.isAuthenticated()) return service.generateToken(loginSchema.getUsername());

        return null;
    }

    public List<Users> getBulk() {
        return repository.findAll();
    }

    public void getStatement() {
        String username = helpers.getCurrentUsername();

        List<Transactions> txns = txnService.findAllByUsername(username);

        Set<TransactionDTO> dtoSet = txns.stream().map(t -> TransactionDTO.builder()
                        .fromAccountNumber(t.getFromAccount().getAccountNumber())
                        .toAccountNumber(t.getToAccount().getAccountNumber())
                        .amount(t.getAmount())
                        .description(t.getDescription())
                        .status(t.getStatus())
                        .currency(t.getCurrency())
                        .createdAt(t.getCreatedAt())
                        .build())
                .collect(Collectors.toSet());

        StatementBody statement = StatementBody.builder()
                .username(username)
                .transactions(dtoSet)
                .build();

        helpers.getStatement(statement,false);

    }
}
