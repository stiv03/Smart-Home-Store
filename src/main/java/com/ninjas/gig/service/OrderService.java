package com.ninjas.gig.service;

import com.ninjas.gig.dto.OrderProductDetailsResponseDTO;
import com.ninjas.gig.dto.OrderRequestDTO;
import com.ninjas.gig.entity.*;
import com.ninjas.gig.repository.OrderProductRepository;
import com.ninjas.gig.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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

    @PersistenceContext
    private EntityManager entityManager;

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

    public List<Order> getAllByClient(UserAccount client) {
        return orderRepository.findByClient(client);
    }

    // служител
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public List<OrderProductDetailsResponseDTO> getOrderProductsDetails(Long orderId) {
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        List<OrderProductDetailsResponseDTO> orderProductDetailsDTOList = new ArrayList<>();

        for (OrderProduct orderProduct : orderProducts) {
            OrderProductDetailsResponseDTO orderProductDetailsDTO = new OrderProductDetailsResponseDTO();
            orderProductDetailsDTO.setProductId(orderProduct.getProduct().getId());
            orderProductDetailsDTO.setProductName(orderProduct.getProduct().getName());
            orderProductDetailsDTO.setQuantity(Math.toIntExact(orderProduct.getQuantity()));
            orderProductDetailsDTO.setPurchasePrice(orderProduct.getPurchasePrice());
            orderProductDetailsDTOList.add(orderProductDetailsDTO);
        }

        return orderProductDetailsDTOList;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatusType newStatus, Long employeeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        UserAccount employee = userService.getUserById(employeeId);
        order.setEmployee(employee);

        switch (newStatus) {
            case PENDING:
                order.setStatus(OrderStatusType.PENDING);
                break;
            case PROCESSING:
                order.setChangeDateTime(LocalDateTime.now());
                order.setStatus(OrderStatusType.PROCESSING);
                break;
            case COMPLETED:
                order.setChangeDateTime(LocalDateTime.now());
                order.setStatus(OrderStatusType.COMPLETED);
                break;
            case CANCELED:
                order.setChangeDateTime(LocalDateTime.now());

                List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
                for (OrderProduct orderProduct : orderProducts) {
                    Product product = productService.getProductById(orderProduct.getProduct().getId());
                    product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
                    productService.saveProduct(product);
                }

                order.setStatus(OrderStatusType.CANCELED);
                break;
            default:
                throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        return orderRepository.save(order);
    }

    // админ
    public BigDecimal calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        Query query = entityManager.createQuery(
                "SELECT COALESCE(SUM(o.value), 0) FROM Order o WHERE o.ChangeDateTime >= :startDate AND o.ChangeDateTime <= :endDate AND o.status = :status",
                BigDecimal.class
        );
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("status", OrderStatusType.COMPLETED);
        return (BigDecimal) query.getSingleResult();
    }
}