package org.example.ecommerceorderandinventory.dto.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateUserDTO {
    @NotBlank
    String username;

    @NotBlank
    @Min(value = 3, message = "Password is too short")
    String password;
}
