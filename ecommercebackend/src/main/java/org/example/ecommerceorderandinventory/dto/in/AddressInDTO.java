package org.example.ecommerceorderandinventory.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddressInDTO {

    @NotNull
    private final long id;

    @NotBlank
    private final String street;
    @NotBlank
    private final String houseNumber;
    @NotBlank
    private final String city;
    @NotBlank
    private final String postalCode;
    @NotBlank
    private final String country;

    public AddressInDTO(long id, String street, String houseNumber, String city, String postalCode, String country) {
        this.id = id;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }
}
