package com.ninjas.gig.service;

import com.ninjas.gig.dto.OrderProductDetailsDTO;
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
import java.util.Map;

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
        Map<Long, Integer> productIdQuantityMap = orderRequestDTO.getProductIdQuantityMap();


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

        for (Map.Entry<Long, Integer> entry : productIdQuantityMap.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productService.getProductById(productId);
            if (product != null && product.getQuantity() >= quantity) {

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
                orderProduct.setPurchasePrice(product.getCurrentPrice());
                orderProduct.setQuantity(Long.valueOf(quantity));

                product.setQuantity(product.getQuantity() - quantity);
                productService.saveProduct(product);
                orderProductRepository.save(orderProduct);

            } else {
                throw new IllegalArgumentException("Product not available in sufficient quantity: " + productId);
            }
        }
        return order;
    }

    @Transactional
    public List<OrderProductDetailsDTO> getOrderProductsDetails(Long orderId) {
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        List<OrderProductDetailsDTO> orderProductDetailsDTOList = new ArrayList<>();

        for (OrderProduct orderProduct : orderProducts) {
            OrderProductDetailsDTO orderProductDetailsDTO = new OrderProductDetailsDTO();
            orderProductDetailsDTO.setProductId(orderProduct.getProduct().getId());
            orderProductDetailsDTO.setProductName(orderProduct.getProduct().getName());
            orderProductDetailsDTO.setQuantity(Math.toIntExact(orderProduct.getQuantity()));
            orderProductDetailsDTO.setPurchasePrice(orderProduct.getPurchasePrice());
            orderProductDetailsDTOList.add(orderProductDetailsDTO);
        }

        return orderProductDetailsDTOList;
    }

    // служител
    public List<Order> getAll() {
        return orderRepository.findAll();
    }


    public List<OrderProduct> getAllOrderProductsByOrderId(Long orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }

    // админ
    public BigDecimal calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByOrderDateTimeBetween(startDate, endDate);
        BigDecimal value = BigDecimal.ZERO;
        for (Order order : orders) {
            if (order.getStatus() == OrderStatusType.COMPLETED) {
                value = value.add(order.getValue());
            }
        }
        return value;
    }

}
