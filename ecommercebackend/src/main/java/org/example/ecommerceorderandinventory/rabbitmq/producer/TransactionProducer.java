package org.example.ecommerceorderandinventory.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceorderandinventory.config.RabbitConfig;
import org.example.ecommerceorderandinventory.rabbitmq.event.TransactionEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publish(TransactionEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }
}
