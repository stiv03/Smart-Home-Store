package com.ninjas.gig.controller;

import com.ninjas.gig.dto.PromotionRequestDTO;
import com.ninjas.gig.dto.UpdatePriceRequestDTO;
import com.ninjas.gig.dto.UpdateQuantityRequestDTO;
import com.ninjas.gig.service.ProductService;
import com.ninjas.gig.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.math.BigDecimal;
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

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @PostMapping("/set-promotion-campaign")
    public ResponseEntity<String> setPromotionalCampaign(@RequestBody PromotionRequestDTO promotionRequest) {
        productService.promotionCampaign(promotionRequest.getProducts(),
                promotionRequest.getPromotionPercent(),
                promotionRequest.getStartDate(),
                promotionRequest.getEndDate());
        return ResponseEntity.ok("Promotional campaign started successfully!");
    }

    // админ
//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/productDelete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/deletedProducts")
    public ResponseEntity<List<Product>> getDeletedProducts() {
        List<Product> products = productService.findDeletedProducts();
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/productReturn/{id}")
    public ResponseEntity<Void> returnProduct(@PathVariable Long id) {
        productService.returnProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/updatePrice/{id}")
    public ResponseEntity<Product> updatePrice(@PathVariable Long id, @RequestBody UpdatePriceRequestDTO updatePriceRequest) {
        var updatedProduct = productService.updateProductCurrentAndMinimalPrice(id, updatePriceRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/updateQuantity/{id}")
    public ResponseEntity<Product> updateQuantity(@PathVariable Long id, @RequestBody UpdateQuantityRequestDTO updateQuantity) {
        var updatedProduct = productService.updateProductQuantity(id, updateQuantity);
        return ResponseEntity.ok(updatedProduct);
    }

//    @PutMapping("/products/{id}/price")
//    public Product updateProductName(@PathVariable Long id, @RequestParam String name) {
//        return productService.updateProductName(id, name);
//    }

}
