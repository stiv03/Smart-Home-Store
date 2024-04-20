package com.ninjas.gig.service;

import com.ninjas.gig.dto.UpdatePriceRequestDTO;
import com.ninjas.gig.dto.UpdateQuantityRequestDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.text.similarity.LevenshteinDistance;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    // методи за OrderService
    @Transactional
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    if (product.isDeleted() || product.getQuantity() == 0) {
                        throw new IllegalArgumentException("Product is deleted or out of stock: " + productId);
                    }


                    product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                    product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                    product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));

                    return product;
                })
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }

    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // клиент
    public List<Product> findAllAvailableProducts() {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.isDeleted = false AND p.quantity > 0",
                Product.class
        );
        List<Product> products = query.getResultList();

        for (Product product : products) {
            product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return products;
    }

    public List<Product> findAllPromotionalProducts() {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.discount > 0 AND p.isDeleted = false AND p.quantity > 0",
                Product.class
        );
        List<Product> products = query.getResultList();

        for (Product product : products) {
            product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return products;
    }

    public List<Product> filterByCategory(String category) {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.isDeleted = false AND p.quantity > 0 AND p.category = :category",
                Product.class
        );
        query.setParameter("category", category);
        List<Product> products = query.getResultList();

        for (Product product : products) {
            product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return products;
    }

    public List<Product> findSimilarProducts(String name) {
        LevenshteinDistance distance = new LevenshteinDistance();
        List<Product> allProducts = findAllAvailableProducts();
        List<Product> similarProducts = new ArrayList<>();

        String[] inputWords = name.toLowerCase().split("\\s+");

        Query query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.isDeleted = false AND p.quantity > 0",
                Product.class
        );
        List<Product> products = query.getResultList();

        for (Product product : products) {
            String productName = product.getName().toLowerCase();
            String[] productWords = productName.split("\\s+");

            for (String inputWord : inputWords) {
                for (String productWord : productWords) {
                    if (distance.apply(inputWord, productWord) <= 2) {
                        similarProducts.add(product);
                        break;
                    }
                }
            }
        }
        return similarProducts;
    }

    // служител
    public Product addProduct(Product product) {
        if (product.getDiscount() == 0) {
            product.setDiscount(0);
        }
        product.setDeleted(false);

        return productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p",
                Product.class
        );
        List<Product> products = query.getResultList();

        for (Product product : products) {
            product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return products;
    }

    public Product findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));

            return product;
        } else {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
    }

    public Product saveChanges(Long id, Product productChanges) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setPhoto(productChanges.getPhoto());
        product.setName(productChanges.getName());
        product.setBrand(productChanges.getBrand());
        product.setModel(productChanges.getModel());
        product.setCategory(productChanges.getCategory());
        product.setDescription(productChanges.getDescription());

        return productRepository.save(product);
    }

    public void promotionCampaign(List<Product> products,
                                  int promotionPercent,
                                  LocalDate startDate,
                                  LocalDate endDate) {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {
            for (Product product : products) {
                promotionSet(product.getId(), promotionPercent);
            }
        } else if (currentDate.isAfter(endDate)) {
            for (Product productss : products) {
                productss.setDiscount(0);
                productRepository.save(productss);
            }
        }
    }

    public void promotionSet(Long id, int newDiscount) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        BigDecimal discountPercentage = BigDecimal.valueOf(newDiscount);
        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100)));

        BigDecimal discountedPrice = product.getOriginalPrice().multiply(discountMultiplier);

        while (product.getMinPrice().compareTo(discountedPrice) > 0) {
            newDiscount--;
            discountPercentage = BigDecimal.valueOf(newDiscount);
            discountMultiplier = BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100)));
            discountedPrice = product.getOriginalPrice().multiply(discountMultiplier);
        }

        product.setDiscount(newDiscount);
        productRepository.save(product);
    }

    // админ
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        product.setDeleted(true);
        productRepository.save(product);
    }

    public List<Product> findDeletedProducts() {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.isDeleted = true",
                Product.class
        );
        List<Product> products = query.getResultList();

        for (Product product : products) {
            product.setMinPrice(product.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setOriginalPrice(product.getOriginalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setCurrentPrice(product.getCurrentPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return products;
    }

    public void returnProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        product.setDeleted(false);
        productRepository.save(product);
    }

    public Product updateProductCurrentAndMinimalPrice(final Long id, final UpdatePriceRequestDTO productChanges) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setCurrentPrice(productChanges.newPrice());
        product.setMinPrice(productChanges.newMinimalPrice());
        product.setDiscount(0);
        return productRepository.save(product);
    }

    public Product updateProductQuantity(final Long id, final UpdateQuantityRequestDTO updateQuantity) {
        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setQuantity(updateQuantity.newQuantity());
        return productRepository.save(product);
    }
}