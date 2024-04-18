package com.ninjas.gig.test;
import com.ninjas.gig.entity.Product;
import com.ninjas.gig.repository.ProductsRepository;
import com.ninjas.gig.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductsRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testApplyPromotion() {
        // Mock data
        Product product1 = new Product(1L, "Product 1", BigDecimal.valueOf(100), BigDecimal.valueOf(80), BigDecimal.valueOf(50));
        Product product2 = new Product(2L, "Product 2", BigDecimal.valueOf(200), BigDecimal.valueOf(150), BigDecimal.valueOf(50));
        List<Product> products = Arrays.asList(product1, product2);

        LocalDate startDate = LocalDate.now().minusDays(1); // Promotion start date (yesterday)
        LocalDate endDate = LocalDate.now().plusDays(1); // Promotion end date (tomorrow)

        // Mocking product repository
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        // Applying promotion
        productService.promotionCampaign(products, 20, startDate, endDate);

        // Verifying that setPromotion method was called for each product
        verify(productRepository, times(1)).save(product1);
        verify(productRepository, times(1)).save(product2);

        // Asserting that the prices are adjusted correctly
        assertEquals(BigDecimal.valueOf(80), product1.getPrice());
        assertEquals(BigDecimal.valueOf(160), product2.getPrice());
    }

    @Test
    public void testSetPromotion() {
        // Mock data
        Product product = new Product(productRepository.findById(1L));

        // Mocking product repository
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        // Setting promotion
        productService.setPromotion(1L, 20);

        // Verifying that product is saved after setting promotion
        verify(productRepository, times(1)).save(product);

        // Asserting that the price is adjusted correctly
        assertEquals(BigDecimal.valueOf(80), product.getPrice());
        assertEquals(20, product.getDiscount());
    }
}
