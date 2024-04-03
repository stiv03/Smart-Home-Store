package com.ninjas.gig.controller;

import com.ninjas.gig.service.ProductService;
import com.ninjas.gig.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class ProductsController {
    @Autowired
    private ProductService productService;

    @PostMapping("/")

    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product addedProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }


    @GetMapping("/")
    public ResponseEntity<List<Product>> displayAllProducts() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Product>> byCategory(@PathVariable String category) {
        List<Product> products = productService.filterByCategory(category);
        return ResponseEntity.ok().body(products);
    }


    @PostMapping("/product/search")
    public ResponseEntity<List<Product>> searchByName(@RequestBody String name) {
        List<Product> similarProducts = productService.findSimilarProducts(name);
        if (similarProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(similarProducts);

    }

}
