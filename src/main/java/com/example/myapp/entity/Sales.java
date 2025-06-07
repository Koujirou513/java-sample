package com.example.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.myapp.enums.Weather;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "販売日は必須です")
    @Column(name = "date", nullable = false, unique = true)
    private LocalDate date; // 販売日

    @NotNull(message = "登録者は必須です")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @ToString.Exclude  // 無限ループを防ぐために除外
    private User createdBy; // 登録者

    @Column(name = "weather", nullable = false)
    @NotNull(message = "天気は必須です")
    @Min(value = 1, message = "天気は1以上で入力してください")
    @Max(value = 5, message = "天気は5以下で入力してください")
    private Integer weather; 

    // 天気情報をテキストで取得
    public String getWeatherText() {
        return Weather.getTextByCode(weather != null ? weather : 0);
    }
    // 天気情報をアイコン付きで取得
    public String getWeatherDisplay() {
        return Weather.getDisplayTextByCode(weather != null ? weather : 0);
    }

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 作成日時

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 更新日時

    // リレーション：１販売実績 -> 多数の販売商品
    @OneToMany(mappedBy = "sales", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude  // 無限ループを防ぐために除外
    @EqualsAndHashCode.Exclude  // 無限ループを防ぐために除外
    private List<SalesItems> salesItems; // 販売商品リスト

    // カスタムコンストラクタ
    public Sales(LocalDate date, User createdBy) {
        this.date = date;
        this.createdBy = createdBy;
    }

    /**
     * 販売個数合計を計算
     */
    public Integer getTotalQuantity() {
        if (salesItems == null || salesItems.isEmpty()) {
            return 0;
        }
        return salesItems.stream()
                .mapToInt(SalesItems::getQuantity)
                .sum();
    }
}
