package com.ninjas.gig.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserAccount clientId;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "order_datetime", nullable = false)
    private LocalDateTime orderDateTime;

    @Formula("(SELECT SUM(order_products.purchase_price) FROM order_products WHERE order_products.order_id = id)")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    private OrderStatusType status = OrderStatusType.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserAccount customerId;

    @Column(name = "completed_datetime")
    private LocalDateTime completedDateTime;
}
