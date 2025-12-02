package org.example.ecommerceorderandinventory.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerceorderandinventory.entity.ItemType;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemDTO {
    @NotNull
    private long itemId;

    private ItemType itemType;

    private String model;

    private int price;

    private int quantity;
}
