package com.eaglebank.eaglebank_api.v1.service.impl;

import com.eaglebank.eaglebank_api.v1.dto.AccountCreateDto;
import com.eaglebank.eaglebank_api.v1.dto.AccountDto;
import com.eaglebank.eaglebank_api.v1.exception.InvalidUserException;
import com.eaglebank.eaglebank_api.v1.model.AccountModel;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.AccountRepository;
import com.eaglebank.eaglebank_api.v1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Optional<AccountDto> getAccountById(String username, Long accountId) {
        validateUser(username);

        AccountModel found = accountRepository.findById(accountId).orElse(null);
        if (found == null) {
            return Optional.empty();
        }
        return Optional.of(toDto(found));

    }

    @Override
    public Optional<List<AccountDto>> getAccountsByUser(String username) {
        UserModel user = validateUser(username);

        List<AccountModel> accounts = accountRepository.findAllByUserId(user.getId());
        if (accounts.isEmpty()) {
            return Optional.empty();
        }
        List<AccountDto> accountDtos = accounts.stream()
                .map(this::toDto)
                .toList();
        return Optional.of(accountDtos);
    }

    @Override
    public AccountDto createAccount(String username, AccountCreateDto accountDto) {
        validateUser(username);

        AccountModel account = AccountModel.builder()
                .name(accountDto.getName())
                .sortCode("10-10-10")
                .currency("GBP")
                .accountType(accountDto.getAccountType())
                .balance(0.0)
                .build();

        AccountModel saved = accountRepository.save(account);
        return toDto(saved);
    }

    @Override
    public AccountDto updateAccount(String username, Long accountId, AccountDto accountDto) {
        validateUser(username);

        AccountModel account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("AccountModel not found"));

        if (accountDto.getAccountType() != null) {
            account.setAccountType(accountDto.getAccountType());
        }
        if (accountDto.getBalance() != null) {
            account.setBalance(accountDto.getBalance());
        }

        AccountModel updated = accountRepository.save(account);
        return toDto(updated);
    }

    @Override
    public void deleteAccount(String username, Long accountId) {
        validateUser(username);

        accountRepository.deleteById(accountId);
    }

    private AccountDto toDto(AccountModel account) {
        return AccountDto.builder()
                .accountNumber(String.valueOf(account.getAccountNumber()))
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .name(account.getName())
                .sortCode(account.getSortCode())
                .createdTimestamp(account.getCreatedTimestamp() != null ? account.getCreatedTimestamp() : null)
                .updatedTimestamp(account.getUpdatedTimestamp() != null ? account.getUpdatedTimestamp() : null)
                .build();
    }

    private UserModel validateUser(String username) {
        UserModel user = userService.getUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("Invalid user: " + username));

        if (!Objects.equals(user.getEmail(), username)) {
            throw new InvalidUserException("User does not match logged in user");
        }
        return user;
    }
}