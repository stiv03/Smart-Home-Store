package com.ninjas.gig.service;

import com.ninjas.gig.entity.Order;
import com.ninjas.gig.entity.Product;
import com.ninjas.gig.repository.OrderRepository;
import com.ninjas.gig.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // клиент


    // служител
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    // админ

}
