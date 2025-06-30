package com.bank.notification_service.repository;

import com.bank.notification_service.models.UserLookup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserLookupRepository extends CrudRepository<UserLookup,String> {

    Optional<UserLookup> findById(String userId);
}
