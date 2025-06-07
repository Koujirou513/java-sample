package com.example.myapp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myapp.entity.User;

@Controller
public class DashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("user", currentUser);
        return "dashboard/index";
    }
}
