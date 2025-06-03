package com.example.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "items")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "商品名は必須です")
    @Size(max = 100, message = "商品名は100文字以内で入力してください")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "価格は必須です")
    @Min(value = 1, message = "価格は1円以上で入力してください")
    @Column(nullable = false)
    private Integer price;

    @NotNull(message = "賞味期限は必須です")
    @Min(value = 1, message = "賞味期限は1日以上で入力してください")
    @Column(name = "shelf_life_day", nullable = false)
    private Integer shelfLifeDay = 15;

    @Size(max = 500, message = "説明は500文字以内で入力してください")
    @Column(length = 500)
    private String description;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // // リレーション：１商品 -> 多数の販売実績
    // @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    // @ToString.Exclude  // 無限ループを防ぐために除外
    // @EqualsAndHashCode.Exclude  // 無限ループを防ぐために除外
    // private List<SalesItem> salesItems; 

    // カスタムコンストラクタ
    public Item(String name, Integer price) {
        this.name = name;
        this.price = price;
        this.shelfLifeDay = 15; // デフォルトの賞味期限
        this.description = ""; // デフォルトの説明
        this.isActive = true; // デフォルトでアクティブ
    }

    public Item(String name, Integer price, Integer shelfLifeDay, String description) {
        this.name = name;
        this.price = price;
        this.shelfLifeDay = shelfLifeDay;
        this.description = description;
        this.isActive = true; // デフォルトでアクティブ
    }
}
