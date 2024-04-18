package com.ninjas.gig.service;

import com.ninjas.gig.entity.Product;
import com.ninjas.gig.exception.ResourceNotFoundException;
import com.ninjas.gig.repository.ProductsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductsRepository productsRepository;

    @Mock
    ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPromotionCampaign_CurrentDateBeforeStartDate() {
        Product product1 = Product.builder()
                .id(1L)
                .brand("idk")
                .model("idk")
                .originalPrice(BigDecimal.valueOf(120))
                .minPrice(BigDecimal.valueOf(50))
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .brand("idk")
                .model("idk")
                .originalPrice(BigDecimal.valueOf(100))
                .minPrice(BigDecimal.valueOf(50))
                .build();
        List<Product> products = Arrays.asList(product1, product2);


        productService.promotionCampaign(products, 20, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30));

        verify(productService, never()).promotionSet(anyLong(), anyInt());

        verify(productsRepository, never()).save(any(Product.class));
    }

    @Test
    void testPromotionCampaign_CurrentDateAfterEndDate() {

        // Mock products
        Product product1 = Product.builder()
                .id(1L)
                .brand("idk")
                .model("idk")
                .originalPrice(BigDecimal.valueOf(120))
                .minPrice(BigDecimal.valueOf(50))
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .brand("idk")
                .model("idk")
                .originalPrice(BigDecimal.valueOf(100))
                .minPrice(BigDecimal.valueOf(50))
                .build();
        List<Product> products = Arrays.asList(product1, product2);


        // Test promotionCampaign method
        productService.promotionCampaign(products, 20, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30));

        // Verify product discounts are set to 0 and saved
        Assertions.assertEquals(0,product1.getDiscount());
        Assertions.assertEquals(BigDecimal.valueOf(120),product1.getOriginalPrice());
    }
    @Test
    public void testPromotionSet() {
        // Mock product
        Product product = Product.builder().id(1L).brand("idk").model("idk").originalPrice(BigDecimal.valueOf(100)).minPrice(BigDecimal.valueOf(50)).build();

        // Mock repository behavior
        when(productsRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        // Test promotionSet method
        productService.promotionSet(1L, 20);

        // Verify product's discount is set
        Assertions.assertEquals(20, product.getDiscount());
        // Verify product is saved
        verify(productsRepository, times(1)).save(product);
    }

    @Test
    void testPromotionSet_ProductNotFound() {
        // Mock repository behavior
        when(productsRepository.findById(1L)).thenReturn(Optional.empty());

        // Call promotionSet method and expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> productService.promotionSet(1L, 20));

        // Verify productRepository.save is not called
        verify(productsRepository, never()).save(any());
    }

    @Test
    void testPromotionSet_DiscountCalculation() {
        // Mock product
        Product product = new Product();
        product.setId(1L);
        product.setOriginalPrice(BigDecimal.valueOf(100));
        product.setMinPrice(BigDecimal.valueOf(50));

        // Mock repository behavior
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Call promotionSet method
        productService.promotionSet(1L, 30);

        // Verify discount is calculated and set correctly
        assertEquals(30, product.getDiscount());

        // Verify productRepository.save is called
        verify(productsRepository, times(1)).save(product);
    }
}
