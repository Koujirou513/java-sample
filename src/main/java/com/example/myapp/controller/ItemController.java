package com.example.myapp.controller;

import com.example.myapp.entity.Item;
import com.example.myapp.entity.User;
import com.example.myapp.repository.ItemRepository;
import com.example.myapp.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 商品一覧ページを表示
     */
    @GetMapping
    public String itemList(Model model) {
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "items/list";
    }

    /**
     * 商品追加ページを表示（管理者のみ）
     */
    @GetMapping("/add")
    public String addItemForm(Model model, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getIsAdmin()) {
            return "redirect:/items?error=permission";
        }

        model.addAttribute("item", new Item());
        return "items/form";
    }
}
