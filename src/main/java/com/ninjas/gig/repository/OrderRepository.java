package com.ninjas.gig.repository;

import com.ninjas.gig.entity.Order;
import com.ninjas.gig.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
