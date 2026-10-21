package com.roelee.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table (name = "orders")
@Data 
public class Order {

    @Id 
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column (name = "customer_id", nullable = false)
    private Long customerId;

    @Column (name = "item_id", nullable = false)
    private Long itemId;

    @Column (name = "quantity", nullable = false)   
    private Integer quantity;
    
    private BigDecimal totalPrice;
    private String status = "NEW";
    private LocalDateTime createdAt;
    
}
