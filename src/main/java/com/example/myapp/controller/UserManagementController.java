package com.example.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myapp.entity.User;
import com.example.myapp.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getIsAdmin()) {
            return "redirect:/dashboard?error=permission";
        }

        model.addAttribute("users", userService.getAllActiveUsers());
        return "admin/users/list";
    }

    @GetMapping("/add")
    public String addUserForm(Model model, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getIsAdmin()) {
            return "redirect:/dashboard?error=permission";
        }
        model.addAttribute("user", new User());
        return "admin/users/form";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute User user, BindingResult result, Model model, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getIsAdmin()) {
            return "redirect:/dashboard?error=permission";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "admin/users/form";
        }

        try {
            userService.createUser(user);
            return "redirect:/admin/users?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/users/form";
        }
    }

    @PostMapping("/edit/{id}")
    public String toggleUserStatus(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getIsAdmin()) {
            return "redirect:/dashboard?error=permission";
        }

        userService.toggleUserStatus(id);
        return "redirect:/admin/users?success";
    }
}
