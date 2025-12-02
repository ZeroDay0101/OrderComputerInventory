package org.example.ecommerceorderandinventory.rabbitmq.event;

import lombok.Getter;

import java.io.Serializable;


@Getter
public class TransactionEvent implements Serializable {
    private final long itemId;
    private final int quantity;
    private final long userId;
    private final long transactionId;

    public TransactionEvent(long itemId, int quantity, long userId, long transactionId) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.userId = userId;
        this.transactionId = transactionId;
    }
}


