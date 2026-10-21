package com.roelee.order.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.roelee.order.entity.Order;

@Service 
public class OrderEventPublisher {

    private static final String TOPIC = "order-created";

    private final KafkaTemplate<String, String> kafkaTemplate;  

    public OrderEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(Order order) {
        if (order.getId() == null) {
            throw new IllegalStateException("Cannot publish an order-created event without an order ID");
        }

        String json = String.format("{\"orderId\": %d, \"customerId\": %d, \"itemId\": %d, \"quantity\": %d, \"totalPrice\": %.2f, \"status\": \"%s\", \"createdAt\": \"%s\"}",
                order.getId(), order.getCustomerId(), order.getItemId(), order.getQuantity(), order.getTotalPrice(), order.getStatus(), order.getCreatedAt());
        kafkaTemplate.send(TOPIC, String.valueOf(order.getId()), json);
    }
}
