package com.ninjas.gig.dto;

import com.ninjas.gig.entity.Product;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PromotionRequestDTO {

    private List<Product> products;
    private int promotionPercent;
    private LocalDate startDate;
    private LocalDate endDate;
}
