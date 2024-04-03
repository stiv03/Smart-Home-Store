package com.ninjas.gig.controller;

import com.ninjas.gig.repository.ResourceNotFoundException;
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

    // клиент
    @GetMapping("/products")
    public ResponseEntity<List<Product>> displayAllProducts() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Product>> displayByCategory(@PathVariable String category) {
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
    // служител
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product addedProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productChanges) {
        Product updatedProduct = productService.saveChanges(id, productChanges);
        return ResponseEntity.ok(updatedProduct);
    }

    // админ
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }



}
