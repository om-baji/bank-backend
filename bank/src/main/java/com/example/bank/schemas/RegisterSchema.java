package com.example.bank.schemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterSchema {

    private String username;

    private String firstName;

    private String lastName;

    private String password;
}
