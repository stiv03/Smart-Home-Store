package com.ninjas.gig.repository;

import com.ninjas.gig.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public interface ProductsRepository extends JpaRepository<Product, Long> {
//    Optional<Product> findFirstByCategoryName(String categoryName);

    List<Product> findByCategory(String category);
//    Optional<Product> findByName(String name);
}
