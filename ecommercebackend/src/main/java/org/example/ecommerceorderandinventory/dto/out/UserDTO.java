package org.example.ecommerceorderandinventory.dto.out;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerceorderandinventory.entity.user.Role;


@Getter
@Setter
@Builder
public class UserDTO {

    private long id;
    private String username;
    private Role role;

    private double balance;
    private AddressOutDTO address;

}
