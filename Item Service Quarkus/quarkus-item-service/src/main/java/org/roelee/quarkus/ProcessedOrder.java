package org.roelee.quarkus;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_orders")
public class ProcessedOrder extends PanacheEntityBase {

    @Id
    public Long id;

    public LocalDateTime processedAt = LocalDateTime.now();
}
