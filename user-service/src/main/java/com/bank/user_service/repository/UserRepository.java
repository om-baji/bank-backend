package com.bank.user_service.repository;

import com.bank.user_service.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users,String> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    List<Users> findAllByIsVerified(Boolean isVerified);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
