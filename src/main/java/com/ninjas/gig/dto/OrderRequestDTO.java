package com.ninjas.gig.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OrderRequestDTO {
    private Long clientId;
    private String address;
    private String phoneNumber;
    private Map<Long, Integer> productIdQuantityMap;
}
