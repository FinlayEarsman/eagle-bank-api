package com.eaglebank.eaglebank_api.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private AddressDto address;
    private String createdTimestamp;
    private String updatedTimestamp;
}