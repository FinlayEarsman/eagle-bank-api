package com.eaglebank.eaglebank_api.v1.dto;

import com.eaglebank.eaglebank_api.v1.model.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegistrationDto {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private AddressDto address;
}
