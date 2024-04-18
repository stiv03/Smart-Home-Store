package com.ninjas.gig.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private UserAccount client;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "order_datetime", nullable = false)
    private LocalDateTime orderDateTime;

    @Formula("(SELECT SUM(order_products.purchase_price * order_products.quantity) FROM order_products WHERE order_products.order_id = id)")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    private OrderStatusType status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private UserAccount employee;

    @Column(name = "change_datetime")
    private LocalDateTime ChangeDateTime;


}
