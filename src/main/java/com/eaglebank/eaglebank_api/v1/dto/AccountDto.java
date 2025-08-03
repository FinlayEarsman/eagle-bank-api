package com.eaglebank.eaglebank_api.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDto {
    private String accountNumber;
    private String name;
    private String sortCode;
    private String accountType;
    private Double balance;
    private String currency;
    private String createdTimestamp;
    private String updatedTimestamp;

}
