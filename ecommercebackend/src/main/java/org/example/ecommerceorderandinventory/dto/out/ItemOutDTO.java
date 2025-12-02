package org.example.ecommerceorderandinventory.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.ecommerceorderandinventory.entity.ItemStatus;
import org.example.ecommerceorderandinventory.entity.ItemType;


@AllArgsConstructor
@Getter
public class ItemOutDTO {
    private final long id;

    private final ItemType itemType;

    private final ItemStatus itemStatus;

    private final String model;

    private final double price;

    private final int quantity;


}
