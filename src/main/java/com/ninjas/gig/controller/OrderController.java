package com.ninjas.gig.controller;

import com.ninjas.gig.dto.OrderProductDetailsDTO;
import com.ninjas.gig.dto.OrderRequestDTO;
import com.ninjas.gig.entity.Order;
import com.ninjas.gig.entity.OrderProduct;
import com.ninjas.gig.entity.Product;
import com.ninjas.gig.service.OrderService;
import com.ninjas.gig.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    // клиент
    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        Order createdOrder = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderProductDetailsDTO>> getOrderProductsDetails(@PathVariable Long orderId) {
        List<OrderProductDetailsDTO> orderProductsDetails = orderService.getOrderProductsDetails(orderId);
        return ResponseEntity.ok(orderProductsDetails);
    }

    // служител
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> displayAllProducts() {
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<OrderProduct>> getOrderProductsByOrderId(@PathVariable Long orderId) {
        List<OrderProduct> orderProducts = orderService.getAllOrderProductsByOrderId(orderId);

        if (orderProducts == null || orderProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orderProducts, HttpStatus.OK);
    }

    // админ
    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        BigDecimal totalRevenue = orderService.calculateTotalRevenue(startDate, endDate);
        return new ResponseEntity<BigDecimal>(totalRevenue, HttpStatus.OK);
    }

}
