package com.eaglebank.eaglebank_api.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserUpdateDto {
    private String email;
    private String name;
    private String phoneNumber;
    private AddressDto address;

    public boolean isValid() {
        return email != null && !email.isEmpty() &&
               name != null && !name.isEmpty() &&
               phoneNumber != null && !phoneNumber.isEmpty() &&
               address != null && address.isValid();
    }

}
