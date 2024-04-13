package com.ninjas.gig.service;

import com.ninjas.gig.entity.Product;
import com.ninjas.gig.repository.ProductsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.ninjas.gig.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository productRepository;

    // методи за OrderService
    @Transactional
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // клиент
    @PersistenceContext
    private EntityManager entityManager;

    public List<Product> findAllAvailableProducts() {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.isDeleted = false AND p.quantity > 0",
                Product.class
        );
        List<Product> products = query.getResultList();

        // Закръгляне на цените до 2 знака след запетаята
        for (Product product : products) {
            product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return products;
    }

    public List<Product> filterByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> findSimilarProducts(String name) {
        LevenshteinDistance distance = new LevenshteinDistance();
        List<Product> allProducts = findAllAvailableProducts();
        List<Product> similarProducts = new ArrayList<>();

        String cleanedName = name.replaceAll("\\s+", "").toLowerCase();

        for (Product product : allProducts) {
            String productName = product.getName().toLowerCase();
            boolean isSimilar = false;

            String[] productWords = productName.split("\\s+");
            for (String productWord : productWords) {
                String cleanedProductWord = productWord.replaceAll("\\s+", "");
                if (distance.apply(cleanedProductWord, cleanedName) <= 2) {
                    isSimilar = true;
                    break;
                }
            }
            if (isSimilar) {
                similarProducts.add(product);
            }
        }
        return similarProducts;
    }

    // служител
    public Product addProduct(Product product){
        if (product.getDiscount() == 0) {
            product.setDiscount(0);
        }
        product.setDeleted(false);

        return productRepository.save(product);
    }

    public Product findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product saveChanges(Long id, Product productChanges) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Обновяване на данните на продукта със зададените промени
        product.setName(productChanges.getName());
        product.setBrand(productChanges.getBrand());
        product.setModel(productChanges.getModel());
        product.setCategory(productChanges.getCategory());
        product.setQuantity(productChanges.getQuantity());
        product.setDescription(productChanges.getDescription());
        product.setMinPrice(productChanges.getMinPrice());
        product.setOriginalPrice(productChanges.getOriginalPrice());
        product.setDiscount(productChanges.getDiscount());

        return productRepository.save(product);
    }

    // админ
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
