package com.ninjas.gig.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Products")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "brand", length = 100, nullable = false)
    private String brand;

    @Column(name = "model", length = 100, nullable = false)
    private String model;

    @Column(name = "category", length = 100, nullable = false)
    private String category;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "min_price", precision = 5, scale = 2, nullable = false)
    private BigDecimal minPrice;

    @Column(name = "original_price", precision = 5, scale = 2, nullable = false)
    private BigDecimal originalPrice;

    @Column(name = "discount")
    private int discount;

    @Transient
    private BigDecimal currentPrice;


    public BigDecimal getCurrentPrice() {
        BigDecimal discountedPrice = originalPrice.subtract(originalPrice.multiply(BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100))));
        return discountedPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
}
