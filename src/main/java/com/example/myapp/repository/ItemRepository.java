package com.example.myapp.repository;

import com.example.myapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

    // 有効なアイテムを取得
    List<Item> findByIsActiveTrueOrderByName();
}
