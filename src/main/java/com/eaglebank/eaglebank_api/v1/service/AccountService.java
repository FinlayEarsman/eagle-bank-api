package com.eaglebank.eaglebank_api.v1.service;

import com.eaglebank.eaglebank_api.v1.dto.AccountCreateDto;
import com.eaglebank.eaglebank_api.v1.dto.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<AccountDto> getAccountById(String username, Long accountId);
    Optional<List<AccountDto>> getAccountsByUser(String username);
    AccountDto createAccount(String username, AccountCreateDto accountDto);
    AccountDto updateAccount(String username, Long accountId, AccountCreateDto accountDto);
    void deleteAccount(String username, Long accountId);
}
