package com.eaglebank.eaglebank_api.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountCreateDto {
    private String name;
    private String accountType;

    public String getInvalidField() {
        if (name == null || name.isEmpty()) {
            return "name";
        }
        if (accountType == null || accountType.isEmpty()) {
            return "accountType";
        }
        return null;
    }
}
