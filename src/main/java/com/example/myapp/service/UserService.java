package com.example.myapp.service;

import com.example.myapp.entity.User;
import com.example.myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    }
    /**
     * ユーザーを新規登録
     */
    public User createUser(User user) {
        if (userRepository.existsByName(user.getName())) {
            throw new RuntimeException("ユーザー名はすでに使用されています");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("メールアドレスはすでに使用されています");
        }
        // パスワードをハッシュ化
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    /**
     * 全ユーザー一覧を取得
     */
    public List<User> getAllActiveUsers() {
        return userRepository.findActiveUsers();
    }

    /*
     * IDでユーザーを取得
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * ユーザー情報更新
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * ログイン時にユーザーの最終ログイン日時を更新
     */
    public void updateLastLogin(String username) {
        userRepository.findByName(username).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    /**
     * ユーザーのステータスをトグル（有効/無効）
     */
    public void toggleUserStatus(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
        });
    }
}
