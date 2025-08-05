package com.eaglebank.eaglebank_api.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class TransactionDto {
    private Double amount;
    private String currency;
    private String type;
    private String reference;

    public boolean isValid() {
        return amount != null && amount > 0 &&
               currency != null && !currency.isEmpty() &&
               type != null && !type.isEmpty() &&
               reference != null && !reference.isEmpty();
    }
}
