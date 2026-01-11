package com.fashion.fashion_backend.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class CombinationRequest {
    private String name;
    private String description;
    private List<Long> productIds;
}
