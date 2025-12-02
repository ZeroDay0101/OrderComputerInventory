package org.example.ecommerceorderandinventory.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.ecommerceorderandinventory.entity.order.OrderStatus;

@AllArgsConstructor
@Getter
public class OrderDTO {

    private long transactionId;

    private long itemId;

    private int quantity;

    private long userId;

    private OrderStatus status;

    private String street;

    private String houseNumber;

    private String city;

    private String postalCode;

    private String country;
}
