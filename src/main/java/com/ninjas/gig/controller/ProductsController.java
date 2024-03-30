package com.ninjas.gig.controller;

import com.ninjas.gig.service.ProductService;
import com.ninjas.gig.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductsController {
    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);

    }

    @GetMapping("/")
    public List<Product> displayAllProducts() {
        return productService.getAll();
    }

    // dependency injection and dependency inversion -> what is that and what it has to do with spring beans

//    @PutMapping("/products/{id}/price")
//    public Product updateProductPrice(@PathVariable Long id, @RequestParam double price) {
//        return productService.updatePrice(id, price);
//    }

    @GetMapping("/{category}")
    public List<Product> byCategory(@PathVariable String category) {
        return productService.filterByCategory(category);
    }


    @GetMapping("/product/search")
    public List<Product> searchByName(@RequestBody String name) {
        List<Product> similarProducts = productService.findSimilarProducts(name);
        return similarProducts;

    }

}
