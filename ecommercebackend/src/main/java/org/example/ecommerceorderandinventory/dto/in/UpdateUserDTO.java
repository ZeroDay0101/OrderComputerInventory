package org.example.ecommerceorderandinventory.dto.in;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.ecommerceorderandinventory.entity.user.Role;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateUserDTO {
    @NotNull
    private long id;

    private String username;
    private Role role;
    private Double balance;

}
