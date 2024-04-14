package com.ninjas.gig.controller;

import com.ninjas.gig.dto.OrderProductDetailsDTO;
import com.ninjas.gig.dto.OrderRequestDTO;
import com.ninjas.gig.dto.OrderStatusChangeDTO;
import com.ninjas.gig.dto.StatisticsRequestDTO;
import com.ninjas.gig.entity.Order;
import com.ninjas.gig.entity.OrderProduct;
import com.ninjas.gig.entity.OrderStatusType;
import com.ninjas.gig.entity.Product;
import com.ninjas.gig.service.OrderService;
import com.ninjas.gig.service.ProductService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok().build();
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
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderProductDetailsDTO>> getOrderProductsDetails(@PathVariable Long orderId) {
        List<OrderProductDetailsDTO> orderProductsDetails = orderService.getOrderProductsDetails(orderId);
        return ResponseEntity.ok(orderProductsDetails);
    }

    @PutMapping("/orderStatusChange/{orderId}")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusChangeDTO statusChangeDTO) {
        Order updatedOrder = orderService.changeOrderStatus(orderId, statusChangeDTO.getNewStatus(), statusChangeDTO.getCustomerId());
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }


    // админ
    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(@Valid @RequestBody StatisticsRequestDTO request) {

        LocalDateTime startDate = request.getStartDate();
        LocalDateTime endDate = request.getEndDate();

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal totalRevenue = orderService.calculateTotalRevenue(startDate, endDate);
        return ResponseEntity.ok(totalRevenue);
    }

}
