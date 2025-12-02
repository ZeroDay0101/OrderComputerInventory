package org.example.ecommerceorderandinventory.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerceorderandinventory.dto.in.ChangeOrderStatusDTO;
import org.example.ecommerceorderandinventory.dto.out.OrderDTO;
import org.example.ecommerceorderandinventory.entity.order.Order;
import org.example.ecommerceorderandinventory.entity.order.OrderStatus;
import org.example.ecommerceorderandinventory.mappers.OrderMapper;
import org.example.ecommerceorderandinventory.repository.*;
import org.example.ecommerceorderandinventory.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private OrderRepository orderRepository;
    private AddressRepository addressRepository;
    private TransactionRepository transactionRepository;
    private OrderMapper orderMapper;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        addressRepository = mock(AddressRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        orderMapper = mock(OrderMapper.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);

        orderService = new OrderService(
                orderRepository,
                addressRepository,
                transactionRepository,
                orderMapper,
                itemRepository,
                userRepository
        );
    }

    // ─────────────────────────────────────────────
    // getOrder()
    // ─────────────────────────────────────────────

    @Test
    void getOrder_success() {
        Order order = new Order();
        OrderDTO expectedDTO = new OrderDTO(1L, 10L, 1, 10, OrderStatus.ONGOING, "Poznańska", "1", "Poznań", "32-111", "POLAND");


        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(expectedDTO);

        OrderDTO result = orderService.getOrder(1L);

        assertEquals(expectedDTO, result);
        verify(orderRepository).findById(1L);
        verify(orderMapper).toOrderDTO(order);
    }

    @Test
    void getOrder_notFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrder(1L)
        );
        verify(orderRepository).findById(1L);
    }

    // ─────────────────────────────────────────────
    // getOrdersByUserId()
    // ─────────────────────────────────────────────

    @Test
    void getOrdersByUserId_success() {
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderDTO> mapped = List.of(
                new OrderDTO(1L, 10L, 1, 10, OrderStatus.ONGOING, "Poznańska", "1", "Poznań", "32-111", "POLAND"),

                new OrderDTO(2L, 10L, 1, 10, OrderStatus.ONGOING, "Poznańska", "1", "Poznań", "32-111", "POLAND")

        );

        when(orderRepository.getUserOrders(10L)).thenReturn(Optional.of(orders));
        when(orderMapper.toOrderDTO(orders)).thenReturn(mapped);

        List<OrderDTO> result = orderService.getOrdersByUserId(10L);

        assertEquals(mapped, result);
        verify(orderRepository).getUserOrders(10L);
        verify(orderMapper).toOrderDTO(orders);
    }

    @Test
    void getOrdersByUserId_userNotFound() {
        when(orderRepository.getUserOrders(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrdersByUserId(10L)
        );

        verify(orderRepository).getUserOrders(10L);
    }

    // ─────────────────────────────────────────────
    // updateOrderStatus()
    // ─────────────────────────────────────────────

    @Test
    void updateOrderStatus_success() {
        Order order = new Order();
        order.setStatus(OrderStatus.PACKAGED);

        ChangeOrderStatusDTO dto = new ChangeOrderStatusDTO(1L, OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(dto);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(orderRepository).findById(1L);
    }

    @Test
    void updateOrderStatus_notFound() {
        ChangeOrderStatusDTO dto = new ChangeOrderStatusDTO(1L, OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.updateOrderStatus(dto)
        );

        verify(orderRepository).findById(1L);
    }
}
