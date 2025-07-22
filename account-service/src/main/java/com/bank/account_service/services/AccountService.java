package com.bank.account_service.services;

import com.bank.account_service.dto.AccountDTO;
import com.bank.account_service.enums.AccountStatus;
import com.bank.account_service.enums.AccountType;
import com.bank.account_service.exceptions.AccountNotFoundException;
import com.bank.account_service.helper.Helper;
import com.bank.account_service.models.Account;
import com.bank.account_service.repository.AccountRepository;
import com.bank.account_service.schemas.PostAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Helper helper;

    @Autowired
    private AccountDTO dto;

    public List<AccountDTO> getAccounts() {

        return accountRepository.findAllByUserId(helper.getUserId())
                .stream()
                .map(account -> dto.mapDTO(account))
                .toList();
    }

    public AccountDTO fetchAccount(String account) throws Exception {
        Optional<Account> exi = accountRepository.findByAccountNumber(account);

        if(exi.isEmpty()) throw new AccountNotFoundException("Account Not Found!");

        return dto.mapDTO(exi.get());
    }

    public AccountDTO createAccount(PostAccount account) throws Exception {
        Account newAccount = Account
                .builder()
                .accountNumber(helper.generateAccountNumber())
                .createdAt(new Date())
                .currency(account.getCurrency())
                .status(AccountStatus.UNAUTHORISED)
                .type(AccountType.valueOf(account.getAccountType()))
                .build();

        return dto.mapDTO(newAccount);
    }
}
