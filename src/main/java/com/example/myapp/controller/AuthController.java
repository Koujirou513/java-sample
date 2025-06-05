package com.example.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myapp.entity.User;

@Controller
public class AuthController {

    /**
     * ログインページを表示
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * ユーザー登録ページを表示
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
}
