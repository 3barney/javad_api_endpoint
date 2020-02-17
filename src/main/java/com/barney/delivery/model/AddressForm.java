package com.barney.delivery.model;

import javax.validation.constraints.NotBlank;

public class AddressForm {

    @NotBlank
    private String type;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String line;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
