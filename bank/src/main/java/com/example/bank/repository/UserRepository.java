package com.example.bank.repository;

import com.example.bank.models.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {
    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
}
