package org.example.ecommerceorderandinventory.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerceorderandinventory.dto.in.ChangeOrderStatusDTO;
import org.example.ecommerceorderandinventory.dto.out.OrderDTO;
import org.example.ecommerceorderandinventory.entity.Item;
import org.example.ecommerceorderandinventory.entity.Transaction;
import org.example.ecommerceorderandinventory.entity.order.Order;
import org.example.ecommerceorderandinventory.entity.order.OrderStatus;
import org.example.ecommerceorderandinventory.entity.user.Address;
import org.example.ecommerceorderandinventory.entity.user.User;
import org.example.ecommerceorderandinventory.mappers.OrderMapper;
import org.example.ecommerceorderandinventory.rabbitmq.event.TransactionEvent;
import org.example.ecommerceorderandinventory.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final TransactionRepository transactionRepository;
    private final OrderMapper orderMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, AddressRepository addressRepository, TransactionRepository transactionRepository, OrderMapper orderMapper, ItemRepository itemRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.transactionRepository = transactionRepository;
        this.orderMapper = orderMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public OrderDTO getOrder(@RequestParam long transactionId) {
        Order order = orderRepository.findById(transactionId).orElseThrow(
                () -> new EntityNotFoundException("Order not found")
        );

        return orderMapper.toOrderDTO(order);
    }

    public List<OrderDTO> getOrdersByUserId(@RequestParam long userId) {
        List<Order> order = orderRepository.getUserOrders(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        return orderMapper.toOrderDTO(order);
    }

    public void createOrder(TransactionEvent transactionEvent) {
        Order order = new Order();

        Transaction transaction = transactionRepository.getReferenceById(transactionEvent.getTransactionId());
        Item item = itemRepository.getReferenceById(transactionEvent.getItemId());
        User user = userRepository.getReferenceById(transactionEvent.getUserId());

        Address address = user.getAddress();


        order.setParentTransaction(transaction);
        order.setItem(item);
        order.setQuantity(transactionEvent.getQuantity());
        order.setUser(user);
        order.setStatus(OrderStatus.ONGOING);

        orderMapper.setAddressOnOrder(order, address);

        orderRepository.save(order);
    }

    public void updateOrderStatus(ChangeOrderStatusDTO changeOrderStatusDTO) {
        Order order = orderRepository.findById(changeOrderStatusDTO.getOrderId()).orElseThrow(
                () -> new EntityNotFoundException("Order not found")
        );

        order.setStatus(changeOrderStatusDTO.getOrderStatus());
    }
}
