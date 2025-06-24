package com.bank.manager_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String id;

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean isVerified;

    private Kyc kyc;

    public UserDTO mappedUser(Users users) {
        return UserDTO.builder()
                .id(users.getId())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email(users.getEmail())
                .isVerified(users.getIsVerified())
                .kyc(users.getKyc())
                .role(users.getRole())
                .build();
    }
}
