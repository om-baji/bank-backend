package com.bank.account_service.controller;

import com.bank.account_service.dto.AccountDTO;
import com.bank.account_service.helper.Helper;
import com.bank.account_service.schemas.PostAccount;
import com.bank.account_service.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private Helper helper;

    @GetMapping("/")
    public List<AccountDTO> fetchAllAccounts() {
        return service.getAccounts();
    }

    @GetMapping("/{account_no}")
    public AccountDTO fetchAccount(@PathVariable String account) throws Exception {
        return service.fetchAccount(account);
    }

    @PostMapping("/create")
    public AccountDTO createAccount(@RequestBody PostAccount postAccount) throws Exception {
        return service.createAccount(postAccount);
    }
}
