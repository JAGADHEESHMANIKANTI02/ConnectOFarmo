package com.example.connectOFarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private String cropName;
    private Double quantityAvailable;
    private Double pricePerUnit;
    private byte[] image;
    private LocalDateTime createdAt;
    private Long userId; // Farmer ID
    private String farmerName; // For display
    private String farmerPhone;
    private String farmerEmail;
}
