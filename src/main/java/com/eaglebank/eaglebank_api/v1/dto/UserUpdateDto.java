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

    public String getInvalidField() {
        if (email == null || email.isEmpty()) {
            return "email";
        }
        if (name == null || name.isEmpty()) {
            return "name";
        }
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "phoneNumber";
        }
        if (address == null || address.getInvalidField() != null) {
            return (address == null) ? "address" : "address." + address.getInvalidField();
        }
        return null;
    }

}
