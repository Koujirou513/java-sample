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
    private ItemRepository itemRepository;

    /**
     * アイテム一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /**
     * 有効なアイテム一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Item> getActiveItems() {
        return itemRepository.findByIsActiveTrueOrderByName();
    }

    /**
     * IDでアイテムを取得
     */
    @Transactional(readOnly = true)
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    /**
     * アイテムを新規登録
     */
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    /**
     * アイテムの更新
     */
    public Item updateItem(Long id, Item item) {
        return itemRepository.save(item);
    }

    /**
     * アイテムのステータスを反転
     */
    public void toggleItemStatus(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("アイテムが見つかりません: " + itemId));
        item.setIsActive(!item.getIsActive());
        itemRepository.save(item);
    }
    

    /**
     * アイテムを物理削除
     */
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("アイテムが見つかりません: " + id);
        }
        itemRepository.deleteById(id);
    }

    /*
     * アイテム数を取得
     */
    public long getActiveItemsCount() {
        return itemRepository.countByIsActiveTrue();
    }
}
