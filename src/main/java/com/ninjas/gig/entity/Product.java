package com.ninjas.gig.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

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

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "technical_documentation", columnDefinition = "TEXT", nullable = false)
    private String technicalDocumentation;

    @Column(name = "min_price", precision = 7, scale = 2, nullable = false)
    private BigDecimal minPrice;

    @Column(name = "original_price", precision = 7, scale = 2, nullable = false)
    private BigDecimal originalPrice;

    @Column(name = "discount", columnDefinition = "INT DEFAULT 0")
    private int discount;

    @Formula(value = "(original_price - (original_price * discount / 100))")
    private BigDecimal currentPrice;
}