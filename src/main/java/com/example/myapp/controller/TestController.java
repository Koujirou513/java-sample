// TestController.java
package com.example.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/test-password")
    public String testPassword() {
        String rawPassword = "admin123";
        String dataHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl.k5uGdTCS";
        
        boolean matches = passwordEncoder.matches(rawPassword, dataHash);
        String newHash = passwordEncoder.encode(rawPassword);
        
        return "<h3>Password Test</h3>" +
                "<p><strong>Raw Password:</strong> " + rawPassword + "</p>" +
                "<p><strong>Database Hash:</strong> " + dataHash + "</p>" +
                "<p><strong>New Generated Hash:</strong> " + newHash + "</p>" +
                "<p><strong>Password Matches:</strong> " + matches + "</p>";
    }
}
