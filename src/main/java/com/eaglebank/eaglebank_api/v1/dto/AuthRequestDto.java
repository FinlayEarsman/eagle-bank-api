package com.eaglebank.eaglebank_api.v1.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {
    private String username;
    private String password;
}
