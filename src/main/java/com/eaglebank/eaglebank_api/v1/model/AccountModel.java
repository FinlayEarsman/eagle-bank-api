package com.eaglebank.eaglebank_api.v1.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;

    private Integer balance;
    private String accountType;
    private String sortCode;
    private String name;
    private String currency;
    @CreationTimestamp
    private String createdTimestamp;
    @UpdateTimestamp
    private String updatedTimestamp;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
}
