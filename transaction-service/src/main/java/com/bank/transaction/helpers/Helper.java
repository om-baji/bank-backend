package com.bank.transaction.helpers;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class Helper {

    private String username;
    private String userId;

    public List<String> getCurrentRoles() {
        return roles;
    }

    public void setCurrentRoles(List<String> roles) {
        this.roles = roles;
    }

    private List<String> roles;

    public void setCurrentId(String id) {
        this.userId = id;
    }

    public void setCurrentUsername(String username) {
        this.username = username;
    }

    public String getCurrentId() {
        return this.userId;
    }

    public String getCurrentUsername() {
        return this.username;
    }
}
