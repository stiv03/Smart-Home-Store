package com.ninjas.gig.repository;

import com.ninjas.gig.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    Optional<Product> findFirstByCategoryName(String categoryName);
}
