package com.bank.manager_service.helpers;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class Helper {

    private String currentUsername;
    private String userId;
    private List<String> roles;
}
