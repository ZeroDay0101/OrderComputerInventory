package org.example.ecommerceorderandinventory.repository;

import org.example.ecommerceorderandinventory.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o from Order o where o.user.id = :userId")
    Optional<List<Order>> getUserOrders(long userId);

}
