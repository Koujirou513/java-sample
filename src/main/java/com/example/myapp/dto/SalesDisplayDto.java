package com.example.myapp.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class SalesDisplayDto {
    private Long id; 
    private LocalDate date; 
    private String createdByName;
    private Integer weather;
    private List<SalesItemDisplayDto> salesItems;
    private Integer totalQuantity; 

    @Data
    public static class SalesItemDisplayDto {
        private String itemName;
        private Integer quantity;
    }
}
