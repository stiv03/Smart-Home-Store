package com.ninjas.gig.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products_photos")
public class ProductPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;

    @Column(name = "photo", length = 255, nullable = false)
    private String photo;

}