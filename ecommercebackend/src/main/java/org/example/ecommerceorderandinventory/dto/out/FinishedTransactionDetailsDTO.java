package org.example.ecommerceorderandinventory.dto.out;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishedTransactionDetailsDTO {
    private long transactionId;

    private long userId;

    private long itemId;

    private int quantity;

    private LocalDateTime date;

    @Override
    public String toString() {
        return "FinishedTransactionDetailsDTO{" +
                "transactionId=" + transactionId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", date=" + date +
                '}';
    }
}
