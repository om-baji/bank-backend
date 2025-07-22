package com.bank.user_service.services;

import com.bank.user_service.helpers.Helper;
import com.bank.user_service.models.Kyc;
import com.bank.user_service.models.UserPrinciple;
import com.bank.user_service.models.Users;
import com.bank.user_service.repository.UserRepository;
import com.bank.user_service.schemas.ApiResponse;
import com.bank.user_service.schemas.LoginSchema;
import com.bank.user_service.schemas.RegisterSchema;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.ImplicitAnyDiscriminatorColumnNameSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Helper helper;

    @Autowired
    private JwtService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<?> saveUser(RegisterSchema registerSchema) {
        log.info(String.valueOf(registerSchema));
        if (userRepository.existsByEmail(registerSchema.getEmail()) ||
                registerSchema.getUsername() != null && userRepository.existsByUsername(registerSchema.getUsername())) {

            log.warn("Registration failed: Username or Email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "Username or Email already exists."));
        }

        Users user = Users.builder()
                .username(registerSchema.getUsername())
                .email(registerSchema.getEmail())
                .firstName(registerSchema.getFirstName())
                .lastName(registerSchema.getLastName())
                .password(passwordEncoder.encode(registerSchema.getPassword()))
                .role("CUSTOMER")
                .isVerified(Boolean.FALSE)
                .kyc(new Kyc())
                .build();

        userRepository.save(user);

        log.info("User created: user_{}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User created! Complete the verification to proceed."));
    }

    public ResponseEntity<?> getProfile() {
        String username = helper.getCurrentUsername();

        Optional<Users> exist = userRepository.findByUsername(username);

        if(exist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false,"User Not Found!"));
        }

        Users user = exist.get();

        Map<String,String> obj = new HashMap<>();
        obj.put("username", user.getUsername());
        obj.put("email", user.getEmail());
        obj.put("first_name",user.getFirstName());
        obj.put("last_name", user.getLastName());
        obj.put("is_verified", user.getIsVerified().toString());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(obj);
    }

    public String loginUser(LoginSchema schema) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    schema.getUsername(), schema.getPassword()
                            )
                    );

            if (!authentication.isAuthenticated()) return null;

            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toUnmodifiableList());

            return service.generateToken(schema.getUsername(), roles, userPrinciple.getUserId());

        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity<?> saveAdmin(RegisterSchema schema) {

        if (userRepository.existsByEmail(schema.getEmail()) ||
                userRepository.existsByUsername(schema.getUsername())) {

            log.warn("Registration failed: Username or Email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "Username or Email already exists."));
        }

        Users user = Users.builder()
                .username(schema.getUsername())
                .email(schema.getEmail())
                .firstName(schema.getFirstName())
                .lastName(schema.getLastName())
                .password(passwordEncoder.encode(schema.getPassword()))
                .role("ADMIN")
                .isVerified(Boolean.TRUE)
                .kyc(new Kyc())
                .build();

        userRepository.save(user);

        log.info("Admin created: user_{}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Admin created!"));
    }

    public ResponseEntity<?> saveManager(RegisterSchema schema) {

        if (userRepository.existsByEmail(schema.getEmail()) ||
                schema.getUsername() != null && userRepository.existsByUsername(schema.getUsername())) {

            log.warn("Registration failed: Username or Email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "Username or Email already exists."));
        }

        Users user = Users.builder()
                .username(schema.getUsername())
                .email(schema.getEmail())
                .firstName(schema.getFirstName())
                .lastName(schema.getLastName())
                .password(passwordEncoder.encode(schema.getPassword()))
                .role("MANAGER")
                .isVerified(Boolean.TRUE)
                .kyc(new Kyc())
                .build();

        userRepository.save(user);

        log.info("Manager created: user_{}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Admin created!"));
    }

}

