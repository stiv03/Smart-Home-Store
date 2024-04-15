package com.ninjas.gig.controller;

import com.ninjas.gig.service.ProductService;
import com.ninjas.gig.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductsController {
    @Autowired
    private ProductService productService;

    // клиент
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> displayAllProducts() {
        List<Product> products = productService.findAllAvailableProducts();
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/promotional")
    public ResponseEntity<List<Product>> getAllPromotionalProducts() {
        List<Product> products = productService.findAllPromotionalProducts();

        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/{category}")
    public ResponseEntity<List<Product>> displayByCategory(@PathVariable String category) {
        List<Product> products = productService.filterByCategory(category);
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchByName(@RequestBody String name) {
        List<Product> similarProducts = productService.findSimilarProducts(name);
        if (similarProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(similarProducts);

    }
    // служител
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<Void> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @GetMapping("/productEmployee")
    public ResponseEntity<List<Product>> getAllProductEmployee() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @GetMapping("productEmployee/{id}")
    public ResponseEntity<Product> getProductByIdEmployee(@PathVariable Long id) {
        Product product = productService.findById(id);

        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productChanges) {
        Product updatedProduct = productService.saveChanges(id, productChanges);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @PostMapping("/set-discount/{id}")
    public ResponseEntity<String> setDiscount(@PathVariable Long id, @RequestBody int newDiscount) {
        productService.promotionSet(id, newDiscount);
        return ResponseEntity.ok("Discount set successfully");
    }

    // админ
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/productDelete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deletedProducts")
    public ResponseEntity<List<Product>> displayDeletedProducts() {
        List<Product> products = productService.findDeletedProducts();
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/productReturn/{id}")
    public ResponseEntity<Void> returnProduct(@PathVariable Long id) {
        productService.returnProduct(id);
        return ResponseEntity.noContent().build();
    }
}
