package com.example.myapp.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalesInputDto {
    
    @NotNull(message = "販売日は必須です")
    private LocalDate date; 

    @NotNull(message = "天気は必須です")
    @Min(value = 1, message = "天気情報が不正です")
    @Max(value = 5, message = "天気情報が不正です")
    private Integer weather; 

    @Valid
    private List<SalesItemDto> salesItems;

    @Data
    public static class SalesItemDto {
        
        @NotNull(message = "商品IDは必須です")
        private Long itemId; // 商品ID

        @NotNull(message = "数量は必須です")
        private Integer quantity; // 数量
    }
}
