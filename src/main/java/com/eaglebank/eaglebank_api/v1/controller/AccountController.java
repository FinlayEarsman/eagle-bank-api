package com.eaglebank.eaglebank_api.v1.controller;


import com.eaglebank.eaglebank_api.v1.dto.AccountCreateDto;
import com.eaglebank.eaglebank_api.v1.dto.AccountDto;
import com.eaglebank.eaglebank_api.v1.dto.AccountListDto;
import com.eaglebank.eaglebank_api.v1.exception.InvalidFieldException;
import com.eaglebank.eaglebank_api.v1.exception.InvalidUserException;
import com.eaglebank.eaglebank_api.v1.service.AccountService;
import com.eaglebank.eaglebank_api.v1.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountCreateDto accountDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        if (accountDto.getInvalidField() != null) {
            throw new InvalidFieldException(accountDto.getInvalidField(), "Missing required data", "String");
        }

        AccountDto created = accountService.createAccount(userEmail, accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountListDto> listAccounts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        List<AccountDto> accounts = accountService.getAccountsByUser(currentUsername).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No accounts found for user")
        );
        return ResponseEntity.ok(AccountListDto.builder().accounts(accounts).build());
    }

    @GetMapping(value="/{accountId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        AccountDto account;
        try {
            account = accountService.getAccountById(currentUsername, accountId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")) ;
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to account");
        }

        return ResponseEntity.ok(account);
    }

    @PatchMapping(value="/{accountId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long accountId, @RequestBody AccountCreateDto accountDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        if (accountDto.getInvalidField() != null) {
            throw new InvalidFieldException(accountDto.getInvalidField(), "Missing required data", "String");
        }

        AccountDto updated;
        try {
            accountService.getAccountById(currentUsername, accountId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
            updated = accountService.updateAccount(currentUsername, accountId, accountDto);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to account");
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value="/{accountId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        try {
            accountService.getAccountById(currentUsername, accountId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
            accountService.deleteAccount(currentUsername, accountId);

        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to account");
        }
        return ResponseEntity.noContent().build();
    }
}