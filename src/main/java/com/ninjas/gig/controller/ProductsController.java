package com.ninjas.gig.controller;

import com.ninjas.gig.service.ProductService;
import com.ninjas.gig.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductsController {
    @Autowired
    private ProductService productService;

    // клиент
    @GetMapping("product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> displayAllProducts() {
        List<Product> products = productService.findAllAvailableProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/promotional")
    public ResponseEntity<List<Product>> getAllPromotionalProducts() {
        List<Product> products = productService.findAllPromotionalProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Product>> displayByCategory(@PathVariable String category) {
        List<Product> products = productService.filterByCategory(category);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchByName(@RequestBody String name) {
        List<Product> similarProducts = productService.findSimilarProducts(name);
        if (similarProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(similarProducts);

    }
    // служител
    @PostMapping("/products")
    public ResponseEntity<Void> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/productEmployee")
    public ResponseEntity<List<Product>> getAllProductEmployee() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("productEmployee/{id}")
    public ResponseEntity<Product> getProductByIdEmployee(@PathVariable Long id) {
        Product product = productService.findById(id);

        return ResponseEntity.ok(product);
    }
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productChanges) {
        Product updatedProduct = productService.saveChanges(id, productChanges);
        return ResponseEntity.ok(updatedProduct);
    }

    // админ

    @PostMapping("/productDelete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/productReturn/{id}")
    public ResponseEntity<Void> returnProduct(@PathVariable Long id) {
        productService.returnProduct(id);
        return ResponseEntity.noContent().build();
    }

}
