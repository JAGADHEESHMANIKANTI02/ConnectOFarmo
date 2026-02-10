package com.example.connectOFarm.dto;

import com.example.connectOFarm.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private Double quantity;
    private Double totalPrice;
    private OrderStatus status;
    private Long postId;
    private String postTitle;
    private Long userId; // Consumer ID
    private String consumerName;
    private String consumerPhone;
}
