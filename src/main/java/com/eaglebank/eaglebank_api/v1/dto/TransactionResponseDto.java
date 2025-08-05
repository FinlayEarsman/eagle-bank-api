package com.eaglebank.eaglebank_api.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class TransactionResponseDto {
    private String id;
    private String accountNumber;
    private String type;
    private Double amount;
    private String currency;
    private String reference;
    private String userId;
    private String createdTimestamp;

}
