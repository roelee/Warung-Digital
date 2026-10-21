package org.roelee.quarkus;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity 
@Table (name = "items")
public class Item extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    public Double price;
    
    public String description;

    public Integer stock;

}
