package com.example.myapp.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.myapp.entity.Item;

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

    /**
     * 全商品の販売数量を初期化（新規作成時用）
     */
    public void initializeWithAllItems(List<Item> items) {
        this.salesItems = new ArrayList<>();
        for (Item item : items) {
            SalesItemDto itemDto = new SalesItemDto();
            itemDto.setItemId(item.getId());
            itemDto.setQuantity(0);  // 初期値は0
            this.salesItems.add(itemDto);
        }
    }

    /*
     * 特定の商品IDの販売数量を取得
     */
    public Integer getQuantityForItem(Long itemId) {
        if (salesItems == null) {
            return 0;
        }
        return salesItems.stream()
                .filter(item -> item.getItemId() != null && item.getItemId().equals(itemId))
                .findFirst()
                .map(SalesItemDto::getQuantity)
                .orElse(0);
    }
}
