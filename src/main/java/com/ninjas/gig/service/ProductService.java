package com.ninjas.gig.service;


import com.ninjas.gig.entity.Product;
import com.ninjas.gig.repository.ProductsRepository;
import com.ninjas.gig.repository.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository productRepository;

    // клиент
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> filterByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> findSimilarProducts(String name) {
        LevenshteinDistance distance = new LevenshteinDistance();
        List<Product> allProducts = getAll();
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
        product.setTechnicalDocumentation(productChanges.getTechnicalDocumentation());
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
