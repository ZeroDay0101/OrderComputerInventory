package org.example.ecommerceorderandinventory.dto.in;

/*
   Class responsible for taking data from users to later extract and create transactions
 */

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreateDTO {
    private long itemId;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;


}
