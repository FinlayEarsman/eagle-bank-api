package com.eaglebank.eaglebank_api.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AccountListDto {
    private List<AccountDto> accounts;


}
