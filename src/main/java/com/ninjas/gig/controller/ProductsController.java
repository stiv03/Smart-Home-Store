package com.ninjas.gig.controller;

import com.ninjas.gig.service.ProductService;
import com.ninjas.gig.repository.ProductsRepository;
import com.ninjas.gig.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductsController {
//    @Autowired
//    private ProductsRepository repository;
    @Autowired
    private ProductService productService;

//    public ProductsController(ProductsRepository repository) {
//        this.repository = repository;
//    }

//    @PostMapping("/")
//    public List<Product> addProduct(@RequestBody ProductService.ProductRequest productRequest) {
//        return productService.addProduct(productRequest);
//
//    }

    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);

    }

    @GetMapping("/")
    public List<Product> displayAllProducts() {
        return productService.getAll();
    }

    // dependency injection and dependency inversion -> what is that and what it has to do with spring beans

    @PutMapping("/products/{id}/price")
    public Product updateProductPrice(@PathVariable Long id, @RequestParam double price) {
        return productService.updatePrice(id, price);
    }

//    @GetMapping("/products/{category}")
//    public Product getSingleProduct(@PathVariable String categoryName){
//        return productService.getProductByCategoryName(categoryName);
//    }

    @GetMapping("/{category}")
    public List<Product> byCategory(@PathVariable String category) {
        return productService.filterByCategory(category);
    }
//    @GetMapping("/product/search")
//    public List<Product> searchByName(@RequestBody String name){
//        List<Product> products = productService.findByName(name);
//        return products;
//    }

    @GetMapping("/product/search")
    public List<Product> searchByName(@RequestBody String name) {
        List<Product> similarProducts = productService.findSimilarProducts(name);
        return similarProducts;

    }

}
