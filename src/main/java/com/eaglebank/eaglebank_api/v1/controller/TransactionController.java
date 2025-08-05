package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.TransactionDto;
import com.eaglebank.eaglebank_api.v1.dto.TransactionListResponseDto;
import com.eaglebank.eaglebank_api.v1.dto.TransactionResponseDto;
import com.eaglebank.eaglebank_api.v1.exception.InvalidUserException;
import com.eaglebank.eaglebank_api.v1.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> executeTransaction(@PathVariable String accountNumber, @RequestBody TransactionDto transactionDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        TransactionResponseDto transaction;
        try {
            transaction = transactionService.executeTransaction(currentUsername, accountNumber, transactionDto);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to account");
        }

        return ResponseEntity.status(201).body(transaction);
    }

    @GetMapping
    public ResponseEntity<TransactionListResponseDto> listTransactions(@PathVariable String accountNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        List<TransactionResponseDto> transactions;
        try {
            transactions = transactionService.getTransactions(currentUsername, accountNumber);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to account");
        }

        return ResponseEntity.ok(TransactionListResponseDto.builder().transactions(transactions).build());
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransaction(@PathVariable String accountNumber, @PathVariable String transactionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        TransactionResponseDto transactionDto;
        try {
            transactionDto = transactionService.getTransaction(currentUsername, accountNumber, transactionId);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to account");
        }
        return ResponseEntity.ok(transactionDto);
    }
}
