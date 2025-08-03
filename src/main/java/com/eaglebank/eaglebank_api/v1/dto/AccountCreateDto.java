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

    public boolean isValid() {
        return name != null && !name.isEmpty() &&
               accountType != null && !accountType.isEmpty();
    }
}
