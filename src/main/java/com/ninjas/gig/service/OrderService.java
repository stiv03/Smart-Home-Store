package com.ninjas.gig.service;

import com.ninjas.gig.dto.OrderRequestDTO;
import com.ninjas.gig.entity.*;
import com.ninjas.gig.repository.OrderProductRepository;
import com.ninjas.gig.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;



    // клиент
    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        Long clientId = orderRequestDTO.getClientId();
        String address = orderRequestDTO.getAddress();
        String phoneNumber = orderRequestDTO.getPhoneNumber();
        List<Long> productIds = orderRequestDTO.getProductIds();

        Order order = new Order();
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusType.PENDING);

        UserAccount client = userService.getUserById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }
        order.setClient(client);

        orderRepository.save(order);

        for (Long productId : productIds) {
            Product product = productService.getProductById(productId);
            if (product != null && product.getQuantity() > 0) {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
                orderProduct.setPurchasePrice(product.getCurrentPrice());

                product.setQuantity(product.getQuantity() - 1);
                productService.saveProduct(product);

                orderProductRepository.save(orderProduct);
            } else {
                throw new IllegalArgumentException("Product not available: " + productId);
            }
        }


        return order;
    }

    // служител
    public List<Order> getAll() {
        return orderRepository.findAll();
    }


    public List<OrderProduct> getAllOrderProductsByOrderId(Long orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }


    // админ

}
