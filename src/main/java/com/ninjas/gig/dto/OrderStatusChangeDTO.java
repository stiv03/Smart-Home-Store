package com.ninjas.gig.dto;

import com.ninjas.gig.entity.OrderStatusType;
import lombok.Data;

@Data
public class OrderStatusChangeDTO {
    private OrderStatusType newStatus;
    private Long employeeId;
}
