package org.example.ecommerceorderandinventory.rabbitmq.consumer;

import org.example.ecommerceorderandinventory.config.RabbitConfig;
import org.example.ecommerceorderandinventory.rabbitmq.event.TransactionEvent;
import org.example.ecommerceorderandinventory.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    private final OrderService orderService;

    public TransactionConsumer(OrderService orderService) {
        this.orderService = orderService;
    }


    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void listen(TransactionEvent event) {
        orderService.createOrder(event);
    }
}
