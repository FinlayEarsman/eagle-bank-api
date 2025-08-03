package com.eaglebank.eaglebank_api.v1.model;

import com.eaglebank.eaglebank_api.v1.dto.AddressDto;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Embeddable
public class Address {
    private String line1;
    private String line2;
    private String line3;
    private String town;
    private String county;
    private String postcode;

    public AddressDto toDto() {
        return AddressDto.builder()
                .line1(this.line1)
                .line2(this.line2)
                .line3(this.line3)
                .town(this.town)
                .county(this.county)
                .postcode(this.postcode)
                .build();
    }
}