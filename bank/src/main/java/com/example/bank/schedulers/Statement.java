package com.example.bank.schedulers;

import com.example.bank.models.StatementBody;
import com.example.bank.models.TransactionDTO;
import com.example.bank.models.Transactions;
import com.example.bank.models.Users;
import com.example.bank.services.TransactionService;
import com.example.bank.services.UserService;
import com.example.bank.util.Helpers;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Statement {

    @Autowired
    private RestTemplate template;

    @Autowired
    private TransactionService txnService;

    @Autowired
    private UserService userService;

    @Autowired
    private Helpers helpers;

    @Scheduled(cron = "0 0 0 1 * *")
// @Scheduled(cron = "*/5 * * * * *")
    public void runJob() {
        System.out.println("Cron job running at " + java.time.LocalTime.now());

        List<Users> usersList = userService.getBulk();

        usersList.forEach(user -> {
            List<Transactions> txns = txnService.findAllByUsername(user.getUsername());

            Set<TransactionDTO> dtoSet = txns.stream().map(t -> TransactionDTO.builder()
                            .fromAccountNumber(t.getFromAccount().getAccountNumber())
                            .toAccountNumber(t.getToAccount().getAccountNumber())
                            .amount(t.getAmount())
                            .description(t.getDescription())
                            .status(t.getStatus())
                            .currency(t.getCurrency())
                            .createdAt(t.getCreatedAt())
                            .build())
                    .collect(Collectors.toSet());

            StatementBody statement = StatementBody.builder()
                    .username(user.getUsername())
                    .transactions(dtoSet)
                    .build();

            helpers.getStatement(statement);
        });
    }

}
