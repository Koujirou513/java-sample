package com.example.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
// import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales_weather")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesWeather {

    @Id
    @Column(name = "target_date")
    private LocalDate targetDate;

    @NotNull(message = "天気は必須です")
    @Min(value = 1, message = "天気は1以上で入力してください")
    @Max(value = 5, message = "天気は5以下で入力してください")
    @Column(nullable = false)
    private Integer condition; // 1:晴れ, 2:曇り, 3:雨, 4:大雨, 5:雪

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt; // 作成日時

    @OneToMany(mappedBy = "salesWeather", fetch = FetchType.LAZY)
    @ToString.Exclude  // 無限ループを防ぐために除外
    @EqualsAndHashCode.Exclude  // 無限ループを防ぐために除外
    private List<Sales> salesList; // リレーション：1つの天気 -> 多数の販売実績

    // カスタムコンストラクタ
    public SalesWeather(LocalDate targetDate, Integer condition) {
        this.targetDate = targetDate;
        this.condition = condition;
    }

    /**
     * 天気コードを文字列に変換
     */
    public String getConditionText() {
        return switch (condition) {
            case 1 -> "晴れ";
            case 2 -> "曇り";
            case 3 -> "雨";
            case 4 -> "大雨";
            case 5 -> "雪";
            default -> "不明";
        };
    }

    /**
     * 天気コードの定数
     */
    public static final class WeatherCondition {
        public static final int SUNNY = 1; // 晴れ
        public static final int CLOUDY = 2; // 曇り
        public static final int RAINY = 3; // 雨
        public static final int HEAVY_RAIN = 4; // 大雨
        public static final int SNOWY = 5; // 雪
    }
}
