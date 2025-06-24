package com.bank.transaction.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserLookup {

    @Id
    private String id;

    private String username;
    private String email;
}
