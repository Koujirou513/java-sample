package com.example.myapp.controller;

import com.example.myapp.entity.Beer;
import com.example.myapp.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerController {

    @Autowired
    private BeerRepository beerRepository;

    /**
     * 全ビール一覧を取得
     */
    @GetMapping
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    /**
     * ビールを新規登録
     */
    @PostMapping
    public Beer creatBeer(@RequestBody Beer beer) {
        return beerRepository.save(beer);
    }
    
    /**
     * データベース接続テスト
     */
    @GetMapping("/test")
    public String testDatabase() {
        long count = beerRepository.count();
        return "データベース接続成功！ビール商品数: " + count + "種類";
    }
}
