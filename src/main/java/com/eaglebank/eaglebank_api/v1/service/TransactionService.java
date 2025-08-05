package com.eaglebank.eaglebank_api.v1.service;

import com.eaglebank.eaglebank_api.v1.dto.TransactionDto;
import com.eaglebank.eaglebank_api.v1.dto.TransactionResponseDto;

import java.util.List;

public interface TransactionService {
    TransactionResponseDto getTransaction(String username, String accountNumber, String transactionId);
    List<TransactionResponseDto> getTransactions(String username, String accountNumber);
    TransactionResponseDto executeTransaction(String username, String accountNumber, TransactionDto transactionCreateDto);
}
