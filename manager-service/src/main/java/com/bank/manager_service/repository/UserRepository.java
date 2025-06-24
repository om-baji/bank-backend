package com.bank.manager_service.repository;

import com.bank.manager_service.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,String> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    List<Users> findAllByIsVerified(Boolean isVerified);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
