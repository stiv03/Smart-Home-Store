package com.ninjas.gig.controller;

import com.ninjas.gig.entity.Order;
import com.ninjas.gig.entity.Product;
import com.ninjas.gig.service.OrderService;
import com.ninjas.gig.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    // клиент


    // служител
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> displayAllProducts() {
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok().body(orders);
    }

    // админ

}
