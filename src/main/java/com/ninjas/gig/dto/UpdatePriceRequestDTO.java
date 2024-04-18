package com.ninjas.gig.dto;

import java.math.BigDecimal;

public record UpdatePriceRequestDTO(BigDecimal newMinimalPrice, BigDecimal newPrice) {}