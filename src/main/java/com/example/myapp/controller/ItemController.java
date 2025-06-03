package com.example.myapp.controller;

import com.example.myapp.entity.Item;
import com.example.myapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository ItemRepository;

    /**
     * 全ビール一覧を取得
     */
    @GetMapping
    public List<Item> getAllItems() {
        return ItemRepository.findAll();
    }

    /**
     * ビールを新規登録
     */
    @PostMapping
    public Item createItem(@RequestBody Item Item) {
        return ItemRepository.save(Item);
    }
    
    /**
     * データベース接続テスト
     */
    @GetMapping("/test")
    public String testDatabase() {
        long count = ItemRepository.count();
        return "データベース接続成功！ビール商品数: " + count + "種類";
    }
}
