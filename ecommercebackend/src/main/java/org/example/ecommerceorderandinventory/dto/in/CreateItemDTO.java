package org.example.ecommerceorderandinventory.dto.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.ecommerceorderandinventory.entity.ItemType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDTO {

    @NotNull
    private ItemType itemType;

    @NotBlank
    private String model;

    @Min(value = 0)
    private double price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;
}
