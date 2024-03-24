package com.ninjas.gig.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public double price;
    public String brand;

//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    public Category category;
    public String category;

    public Product() {}

//    public Product(String name, double price, String brand, Category category) {
//        this.name = name;
//        this.price = price;
//        this.brand = brand;
//        this.category = category;
//    }
public Product(String name, double price, String brand, String category) {
    this.name = name;
    this.price = price;
    this.brand = brand;
    this.category = category;
}
}
