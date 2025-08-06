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

    public String getInvalidField () {
        if (amount == null || amount <= 0) {
            return "amount";
        }
        if (currency == null || currency.isEmpty()) {
            return "currency";
        }
        if (type == null || type.isEmpty()) {
            return "type";
        }
        if (reference == null || reference.isEmpty()) {
            return "reference";
        }
        return null;
    }
}
