package com.eaglebank.eaglebank_api.v1.dto;


import com.eaglebank.eaglebank_api.v1.model.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDto {
    private String line1;
    private String line2;
    private String line3;
    private String town;
    private String county;
    private String postcode;

    public Address toModel() {
        return Address.builder()
                .line1(this.line1)
                .line2(this.line2)
                .line3(this.line3)
                .town(this.town)
                .county(this.county)
                .postcode(this.postcode)
                .build();
    }

    public String getInvalidField() {
        if (line1 == null || line1.isEmpty()) {
            return "line1";
        }
        if (line2 == null) {
            return "line2";
        }
        if (line3 == null) {
            return "line3";
        }
        if (town == null || town.isEmpty()) {
            return "town";
        }
        if (county == null || county.isEmpty()) {
            return "county";
        }
        if (postcode == null || postcode.isEmpty()) {
            return "postcode";
        }
        return null;
    }
}