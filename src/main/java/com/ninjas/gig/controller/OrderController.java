package com.ninjas.gig.controller;

import com.ninjas.gig.dto.OrderProductDetailsResponseDTO;
import com.ninjas.gig.dto.OrderRequestDTO;
import com.ninjas.gig.dto.OrderStatusChangeDTO;
import com.ninjas.gig.dto.StatisticsRequestDTO;
import com.ninjas.gig.entity.*;
import com.ninjas.gig.service.OrderService;
import com.ninjas.gig.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // клиент
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/createOrder")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/clientOrder/{clientId}")
    public ResponseEntity<List<Order>> getOrdersByClientId(@PathVariable Long clientId) {
        UserAccount client = userService.getUserById(clientId);
        List<Order> orders = orderService.getAllByClient(client);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    // служител
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> displayAllOrders() {
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok().body(orders);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER','EMPLOYEE','ADMIN')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderProductDetailsResponseDTO>> getOrderProductsDetails(@PathVariable Long orderId) {
        List<OrderProductDetailsResponseDTO> orderProductsDetails = orderService.getOrderProductsDetails(orderId);
        return ResponseEntity.ok(orderProductsDetails);
    }
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @PutMapping("/orderStatusChange/{orderId}")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusChangeDTO statusChangeDTO) {
        Order updatedOrder = orderService.changeOrderStatus(orderId, statusChangeDTO.getNewStatus(), statusChangeDTO.getEmployeeId());
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // админ
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/revenue")
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
