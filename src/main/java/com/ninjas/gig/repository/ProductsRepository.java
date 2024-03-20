package com.ninjas.gig.repository;

import com.ninjas.gig.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Long> {
}
