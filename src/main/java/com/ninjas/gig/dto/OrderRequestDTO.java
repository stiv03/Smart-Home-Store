package com.ninjas.gig.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private Long clientId;
    private String address;
    private String phoneNumber;
    private List<Long> productIds;
}
