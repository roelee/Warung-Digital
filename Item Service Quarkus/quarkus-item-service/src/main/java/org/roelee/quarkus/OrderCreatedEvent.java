package org.roelee.quarkus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCreatedEvent {

    public Long orderId;
    public Long customerId;
    public Long itemId;
    public Integer quantity;
    public String createdAt;

}
