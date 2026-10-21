package org.roelee.quarkus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderEventConsumer {

    private static final Logger LOGGER = Logger.getLogger(OrderEventConsumer.class);

    @Inject
    ObjectMapper mapper;

    @Incoming("order-created")
    @Blocking
    @Transactional
    public void onOrderCreated(String payload) throws JsonProcessingException {
        OrderCreatedEvent event = mapper.readValue(payload, OrderCreatedEvent.class);
        LOGGER.infof("Received order created event: order %d, item %d, qty %d", event.orderId, event.itemId, event.quantity);

        if (ProcessedOrder.findById(event.orderId) != null) {
            LOGGER.warnf("Order with ID: %d has already been processed.", event.orderId);
            return;
        } 

        Item item = Item.findById(event.itemId);
        if (item == null) { 
            LOGGER.warnf("Item with ID: %s not found.", event.itemId);
            return;
        }

        if (item.stock < event.quantity) {
            LOGGER.warnf("Not enough stock for item ID: %s. Available: %s, Requested: %s", event.itemId, item.stock, event.quantity);
            return;
        }

        item.stock = item.stock - event.quantity;

        ProcessedOrder done = new ProcessedOrder();
        done.id = event.orderId;
        done.persist();
        LOGGER.infof("Processed order with ID: %d", event.orderId);
    }

}
