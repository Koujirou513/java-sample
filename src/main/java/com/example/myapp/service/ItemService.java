package com.example.myapp.service;

import com.example.myapp.entity.Item;
import com.example.myapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ItemService {
    
    @Autowired
    private ItemRepository ItemRepository;

    /**
     * アイテム一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        return ItemRepository.findAll();
    }

    /**
     * 有効なアイテム一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Item> getActiveItems() {
        return ItemRepository.findByIsActiveTrueOrderByName();
    }

    /**
     * IDでアイテムを取得
     */
    @Transactional(readOnly = true)
    public Optional<Item> getItemById(Long id) {
        return ItemRepository.findById(id);
    }

    /**
     * アイテムを新規登録
     */
    public Item createItem(Item item) {
        return ItemRepository.save(item);
    }

    /**
     * アイテムの更新
     */
    public Item updateItem(Long id, Item item) {
        if (!ItemRepository.existsById(id)) {
            throw new RuntimeException("アイテムが見つかりません: " + id);
        }
        return ItemRepository.save(item);
    }

    /**
     * アイテムのステータスを反転
     */
    public void toggleItemStatus(Long itemId) {
        Item item = ItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("アイテムが見つかりません: " + itemId));
        item.setIsActive(!item.getIsActive());
        ItemRepository.save(item);
    }
    

    /**
     * アイテムを物理削除
     */
    public void deleteItem(Long id) {
        if (!ItemRepository.existsById(id)) {
            throw new RuntimeException("アイテムが見つかりません: " + id);
        }
        ItemRepository.deleteById(id);
    }
}
