package com.bank.notification_service.repository;

import com.bank.notification_service.models.UserLookup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserLookupRepository extends CrudRepository<String, UserLookup> {

    Optional<UserLookup> findByUserId(String userId);
}
