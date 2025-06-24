package com.example.bank.schemas;

import com.example.bank.models.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {

    private String username;

    private String firstName;

    private String lastName;

    private Date timestamp;

    public static RegisterResponse fromUserDTO(Users user) {

        return RegisterResponse
                .builder()
                .firstName(user.getFirst_name())
                .lastName(user.getLast_name())
                .timestamp(new Date())
                .username(user.getUsername())
                .build();
    }

}
