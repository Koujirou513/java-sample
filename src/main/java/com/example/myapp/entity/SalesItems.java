package com.example.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "sales_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesItems {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "販売実績は必須です")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id", nullable = false)
    @ToString.Exclude  // 無限ループを防ぐために除外
    private Sales sales; // 販売実績

    @NotNull(message = "商品は必須です")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude  // 無限ループを防ぐために除外
    private Item item; // 商品


    @Min(value = 0, message = "数量は以上で入力してください")
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer quantity = 0; // 数量

    // カスタムコンストラクタ
    public SalesItems(Sales sales, Item item, Integer quantity) {
        this.sales = sales;
        this.item = item;
        this.quantity = quantity != null ? quantity : 0; // 数量がnullの場合は0を設定
    }
}
