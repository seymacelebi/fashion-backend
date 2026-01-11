package com.fashion.fashion_backend.entity.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CombinationResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    private List<ProductSummaryDTO> products;

    private Double totalValue;

    @Data
    @Builder
    public static class ProductSummaryDTO {
        private Long id;
        private String name;
        private String imageUrl;
        private String categoryName;
        private String brandName;
        private Double price;
    }
}
