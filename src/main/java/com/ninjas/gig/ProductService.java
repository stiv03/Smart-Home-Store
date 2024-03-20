package com.ninjas.gig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository productRepository;

    public Product updatePrice(Long id, double newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

}
