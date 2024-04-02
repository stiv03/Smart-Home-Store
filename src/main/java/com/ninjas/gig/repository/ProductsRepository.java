package com.ninjas.gig.repository;

import com.ninjas.gig.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductsRepository extends JpaRepository<Product, Long> {
//    Optional<Product> findFirstByCategoryName(String categoryName);

    List<Product> findByCategory(String category);
//    Optional<Product> findByName(String name);
}
