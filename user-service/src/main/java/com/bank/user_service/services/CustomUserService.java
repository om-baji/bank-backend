package com.bank.user_service.services;

import com.bank.user_service.models.UserDTO;
import com.bank.user_service.models.UserPrinciple;
import com.bank.user_service.models.Users;
import com.bank.user_service.repository.UserRepository;
import com.sun.security.auth.UserPrincipal;
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
        Optional<Users> exist = repository.findByUsername(username);
        if (exist.isEmpty()) return null;

        Users user = exist.get();

        return new UserPrinciple(user);
    }
}
