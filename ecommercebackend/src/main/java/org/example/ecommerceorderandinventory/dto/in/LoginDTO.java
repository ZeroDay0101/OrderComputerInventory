package org.example.ecommerceorderandinventory.dto.in;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private final String username;
    private final String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
