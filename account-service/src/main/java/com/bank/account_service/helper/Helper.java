package com.bank.account_service.helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Setter
@Getter
@Component
public class Helper {

    private String userId;
    private String email;

    public String generateAccountNumber() {
        String prefix = "ACC";
        long timestamp = System.currentTimeMillis();
        int randomSuffix = new Random().nextInt(900) + 100;
        return prefix + timestamp + randomSuffix;
    }
}
