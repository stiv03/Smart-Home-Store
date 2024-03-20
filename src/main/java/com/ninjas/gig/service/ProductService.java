package com.ninjas.gig.service;

import com.ninjas.gig.model.Product;
import com.ninjas.gig.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired

    private ProductsRepository productRepository;
    public record ProductRequest(String name, double price, String brand) {}

    //ResponseEntity or Product repository

//    public ResponseEntity<?> addProduct(ProductRequest productRequest) {
//        Product newProduct = new Product(productRequest.name, productRequest.price, productRequest.brand);
//        productRepository.save(newProduct);
//        return ResponseEntity.ok(productRepository.findAll());
//    }
    public List<Product> addProduct(ProductRequest productRequest) {
        Product newProduct = new Product(productRequest.name, productRequest.price, productRequest.brand);
        productRepository.save(newProduct);
        return productRepository.findAll();
    }

    public List<Product> displayAll() {
        return productRepository.findAll();
    }

    public Product updatePrice(Long id, double newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        product.setPrice(newPrice);
        return productRepository.save(product);
    }



}
