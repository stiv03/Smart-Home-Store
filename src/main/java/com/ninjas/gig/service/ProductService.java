package com.ninjas.gig.service;


//import com.ninjas.gig.model.Category;
import com.ninjas.gig.model.Product;
//import com.ninjas.gig.repository.CategoryRepository;
import com.ninjas.gig.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository productRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    public record ProductRequest(String name, double price, String brand, String category) {}

//    public List<Product> addProduct(ProductRequest productRequest) {
//        Category category = new Category();
//        category.setName(productRequest.category);
//        Product newProduct = new Product(productRequest.name, productRequest.price, productRequest.brand, category);
//        categoryRepository.save(category);
//        productRepository.save(newProduct);
//        return productRepository.findAll();
//    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public Product updatePrice(Long id, double newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

//    public Product getProductByCategoryName(String categoryName) {
//        return productRepository.findFirstByCategoryName(categoryName)
//                .orElseThrow(() -> new RuntimeException("Product not found in category: " + categoryName));
//    }

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

}
