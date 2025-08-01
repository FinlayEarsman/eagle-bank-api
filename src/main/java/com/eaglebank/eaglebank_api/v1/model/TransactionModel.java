package com.eaglebank.eaglebank_api.v1.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String transactionType;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel account;
}