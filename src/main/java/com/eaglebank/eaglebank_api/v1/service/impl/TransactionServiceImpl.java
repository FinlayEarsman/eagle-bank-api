package com.eaglebank.eaglebank_api.v1.service.impl;

import com.eaglebank.eaglebank_api.v1.dto.TransactionDto;
import com.eaglebank.eaglebank_api.v1.dto.TransactionResponseDto;
import com.eaglebank.eaglebank_api.v1.model.AccountModel;
import com.eaglebank.eaglebank_api.v1.model.TransactionModel;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.AccountRepository;
import com.eaglebank.eaglebank_api.v1.repository.TransactionRepository;
import com.eaglebank.eaglebank_api.v1.repository.UserRepository;
import com.eaglebank.eaglebank_api.v1.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TransactionResponseDto getTransaction(String username, String accountNumber, String transactionId) {
        UserModel user = userRepository.findByEmail(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + username);
        }
        TransactionModel transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new IllegalArgumentException("Transaction not found with ID: " + transactionId)
        );
        AccountModel account = accountRepository.findById(Long.valueOf(accountNumber)).orElseThrow(
                () -> new IllegalArgumentException("Account not found with number: " + accountNumber)
        );
        if (!transaction.getAccount().getAccountNumber().equals(account.getAccountNumber())) {
            throw new IllegalArgumentException("Transaction does not belong to the specified account");
        }
        return toDto(transaction, user.getId());
    }

    @Override
    public List<TransactionResponseDto> getTransactions(String username, String accountNumber) {
        UserModel user = userRepository.findByEmail(username);
        AccountModel account = accountRepository.findById(Long.valueOf(accountNumber)).orElseThrow(() -> new IllegalArgumentException("Account not found with number: " + accountNumber));

        List<TransactionModel> transactions = transactionRepository.findAllByAccount(account);
        return transactions.stream()
                .map(item -> toDto(item, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponseDto executeTransaction(String username, String accountNumber, TransactionDto transactionDto) {
        UserModel user = userRepository.findByEmail(username);
        AccountModel account = accountRepository.findById(Long.valueOf(accountNumber)).orElseThrow(() -> new IllegalArgumentException("Account not found with number: " + accountNumber));
        if (!transactionDto.isValid()) {
            throw new IllegalArgumentException("Transaction invalid");
        }

        if ("deposit".equalsIgnoreCase(transactionDto.getType())) {
            account.setBalance((int) (account.getBalance() + transactionDto.getAmount()));
        } else if ("withdrawal".equalsIgnoreCase(transactionDto.getType())) {
            if (transactionDto.getAmount().compareTo(Double.valueOf(account.getBalance())) > 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            account.setBalance((int) (account.getBalance() - transactionDto.getAmount()));
        }
        accountRepository.save(account);

        String tan;
        do {
            tan = generateTan();
        } while (transactionRepository.existsById(tan));

        TransactionModel transaction = TransactionModel.builder()
                .id(tan)
                .userId(String.valueOf(user.getId()))
                .amount(transactionDto.getAmount())
                .currency(transactionDto.getCurrency())
                .reference(transactionDto.getReference())
                .type(transactionDto.getType())
                .account(account)
                .build();
        return toDto(transactionRepository.save(transaction), user.getId());
    }

    public static String generateTan() {
        return "tan-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    private TransactionResponseDto toDto(TransactionModel transaction, Long userId) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .accountNumber(String.valueOf(transaction.getAccount().getAccountNumber()))
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .reference(transaction.getReference())
                .userId(String.valueOf(userId))
                .createdTimestamp(String.valueOf(transaction.getCreatedTimestamp()))
                .build();
    }
}