package com.example.myapp.entity;
// JPA関連
import jakarta.persistence.*;
// バリデーション関連
import jakarta.validation.constraints.*;
// Spring Data JPAの監査機能
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
// 日時API
import java.time.LocalDateTime;

@Entity
@Table(name = "beers")
@EntityListeners(AuditingEntityListener.class)
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ビール名は必須です")
    @Size(max = 100, message = "ビール名は100文字以内で入力してください")
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


    public Beer() {}

    public Beer(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Beer(String name, Integer price, Integer shelfLifeDay, String description) {
        this.name = name;
        this.price = price;
        this.shelfLifeDay = shelfLifeDay;
        this.description = description;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Integer getShelfLifeDay() {
        return shelfLifeDay;
    }
    public void setShelfLifeDay(Integer shelfLifeDay) {
        this.shelfLifeDay = shelfLifeDay;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    @Override
    public String toString() {
        return "Beer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", shelfLifeDay=" + shelfLifeDay +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
