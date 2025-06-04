package com.example.myapp.service;

import com.example.myapp.entity.User;
import com.example.myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
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

    public List<User> getAllActiveUsers() {
        return userRepository.findActiveUsers();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void updateLastLogin(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + userId);
        }
    }

    public void toggleUserStatus(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
        });
    }
}
