package org.example.ecommerceorderandinventory.entity;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private String model;

    private double price;

    private int quantity;


}
