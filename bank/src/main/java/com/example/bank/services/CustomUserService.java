package com.example.bank.services;

import com.example.bank.models.UserPrincipal;
import com.example.bank.models.Users;
import com.example.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> exists = repository.findByUsername(username);

        if(exists.isEmpty()) throw new UsernameNotFoundException("No user found!");

        return new UserPrincipal(exists.get());
    }
}

