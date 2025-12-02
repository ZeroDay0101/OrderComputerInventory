package org.example.ecommerceorderandinventory.dto.in;

import lombok.Getter;
import org.example.ecommerceorderandinventory.entity.order.OrderStatus;

@Getter
public class ChangeOrderStatusDTO {
    private final long orderId;
    private final OrderStatus orderStatus;

    public ChangeOrderStatusDTO(long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
