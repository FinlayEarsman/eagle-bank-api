package com.eaglebank.eaglebank_api.v1.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {
    @Id
    private String id;

    private Double amount;
    private String type;
    private String reference;
    private String currency;
    private String userId;
    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel account;
}