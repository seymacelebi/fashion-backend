package com.fashion.fashion_backend.entity.dto;


public class CategoryDTOs {


    public static record CategoryCreateDto(
            String name
    ) {
    }

    public static record CategoryDto(
            Long id,
            String name
    ) {
    }
}
