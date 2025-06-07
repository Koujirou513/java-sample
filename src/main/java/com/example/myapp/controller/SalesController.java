package com.example.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myapp.dto.SalesInputDto;
import com.example.myapp.entity.User;
import com.example.myapp.service.ItemService;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public String salesList(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("message", "販売実績一覧ページ");
        return "sales/list"; 
    }

    @GetMapping("/input")
    public String salesInput(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("salesInput", new SalesInputDto());

        model.addAttribute("items", itemService.getActiveItems());
        
        model.addAttribute("message", "販売実績入力ページ");
        return "sales/input"; 
    }
}
