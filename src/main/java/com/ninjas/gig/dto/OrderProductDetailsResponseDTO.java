package com.ninjas.gig.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDetailsResponseDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal purchasePrice;
}
