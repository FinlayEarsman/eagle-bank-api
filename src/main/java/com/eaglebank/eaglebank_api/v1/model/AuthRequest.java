package com.eaglebank.eaglebank_api.v1.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;
}
