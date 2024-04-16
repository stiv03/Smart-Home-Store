package com.ninjas.gig.repository;

import com.ninjas.gig.entity.Order;
import com.ninjas.gig.entity.Product;
import com.ninjas.gig.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByClient(UserAccount client);
}
