package com.example.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 100, message = "ユーザー名は100文字以下で入力してください")
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, max = 100, message = "パスワードは6文字以上100文字以下で入力してください")
    @Column(nullable = false, length = 100)
    private String password;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @NotNull
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @ToString.Exclude  // 無限ループを防ぐために除外
    @EqualsAndHashCode.Exclude  // 無限ループを防ぐために除外
    private List<Sales> salesList;

    // カスタムコンストラクタ
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
        this.isActive = true; // デフォルトでアクティブ
    }

    public User(String name, String email, String password, Boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isActive = true; // デフォルトでアクティブ
    }
}
