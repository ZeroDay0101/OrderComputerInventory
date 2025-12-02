package org.example.ecommerceorderandinventory.dto.out;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressOutDTO {
    private long id;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String country;
}
