package com.ninjas.gig;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;
    public long price;

    public String brand;

    public Product() {}

    public Product(String name, long price, String brand) {
        this.name = name;
        this.price = price;
        this.brand = brand;
    }

}
