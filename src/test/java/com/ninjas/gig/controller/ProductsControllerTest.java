package com.ninjas.gig.controller;

import com.ninjas.gig.entity.Product;
import com.ninjas.gig.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductsController productsController;

    @BeforeEach
    void setUp() {
        // Optional: You can reset mocks before each test
        reset(productService);
    }

    @Test
    void getProductById_ProductExists_ReturnsProduct() {
        // Arrange
        Long productId = 1L;
        Product expectedProduct = new Product(); // create an instance of expected product
        when(productService.getProductById(productId)).thenReturn(expectedProduct);

        // Act
        ResponseEntity<Product> responseEntity = productsController.getProductById(productId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProduct, responseEntity.getBody());
    }

    @Test
    void getProductById_ProductNotExists_ReturnsNotFound() {
        // Arrange
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(null);

        // Act
        ResponseEntity<Product> responseEntity = productsController.getProductById(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void displayAllProducts_ReturnsListOfProducts() {
        // Arrange
        List<Product> expectedProducts = new ArrayList<>(); // create a list of expected products
        when(productService.findAllAvailableProducts()).thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<Product>> responseEntity = productsController.displayAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProducts, responseEntity.getBody());
    }

    @Test
    void getAllPromotionalProducts_ReturnsListOfPromotionalProducts() {
        // Arrange
        List<Product> expectedPromotionalProducts = new ArrayList<>(); // create a list of expected promotional products
        when(productService.findAllPromotionalProducts()).thenReturn(expectedPromotionalProducts);

        // Act
        ResponseEntity<List<Product>> responseEntity = productsController.getAllPromotionalProducts();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPromotionalProducts, responseEntity.getBody());
    }

    @Test
    void getProductById_ProductDoesNotExist_ReturnsNotFound() {
        // Arrange
        long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(null);

        // Act
        ResponseEntity<Product> responseEntity = productsController.getProductById(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void displayAllProducts_ReturnsAllProducts() {
        // Arrange
        List<Product> expectedProducts = new ArrayList<>(); // create a list of expected products
        when(productService.findAllAvailableProducts()).thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<Product>> responseEntity = productsController.displayAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProducts, responseEntity.getBody());
    }

    @Test
    void getAllPromotionalProducts_ReturnsPromotionalProducts() {
        // Arrange
        List<Product> expectedProducts = new ArrayList<>(); // create a list of expected promotional products
        when(productService.findAllPromotionalProducts()).thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<Product>> responseEntity = productsController.getAllPromotionalProducts();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProducts, responseEntity.getBody());
    }
}

