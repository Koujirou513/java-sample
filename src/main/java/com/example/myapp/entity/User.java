package com.example.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

// import org.apache.catalina.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
    
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isAdmin) {
            return Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        } else {
            return Collections.singletonList(new SimpleGrantedAuthority("USER"));
        }
    }

    @Override
    public String getUsername() {
        return name;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true; // アカウントの有効期限はなし
    }
    @Override
    public boolean isAccountNonLocked() {
        return isActive; // isActiveがtrueならアカウントはロックされていない
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 資格情報の有効期限はなし
    }
    @Override
    public boolean isEnabled() {
        return isActive; // isActiveがtrueならアカウントは有効
    }
}
