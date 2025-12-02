package org.example.ecommerceorderandinventory.entity.order;

import jakarta.persistence.*;
import lombok.*;
import org.example.ecommerceorderandinventory.entity.Item;
import org.example.ecommerceorderandinventory.entity.Transaction;
import org.example.ecommerceorderandinventory.entity.user.User;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "transaction_id")
    private Transaction parentTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String street;

    private String houseNumber;

    private String city;

    private String postalCode;

    private String country;

}
