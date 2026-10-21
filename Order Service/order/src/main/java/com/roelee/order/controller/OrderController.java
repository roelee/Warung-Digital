package com.roelee.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.roelee.order.entity.Order;
import com.roelee.order.repository.OrderRepository;
import com.roelee.order.service.OrderEventPublisher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repository;

    private final RestClient customerClient = RestClient.create("http://localhost:8081");

    private final OrderEventPublisher eventPublisher;

    public OrderController(OrderRepository repository, OrderEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public List<Order> getAll(@RequestParam(required = false) Long customerId) {
        return customerId == null ? repository.findAll() : repository.findByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order) {
        /*
        try {
            // Check if the customer exists by making a GET request to the Customer Service
            customerClient.get()
                    .uri("/customers/{id}", order.getCustomerId())
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found");
        }
        */
        order.setId(null);
        Order savedOrder = repository.saveAndFlush(order);
        eventPublisher.publishOrderCreated(savedOrder);
        return savedOrder;
    }

    @PutMapping("/{id}/status")
    public Order updateOrder(@PathVariable Long id, @RequestParam String status) {
       
        Order existing = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        existing.setStatus(status);
        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Order existing = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        repository.delete(existing);
    }
}