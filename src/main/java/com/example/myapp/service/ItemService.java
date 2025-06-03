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
     * 全ビール一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        return ItemRepository.findAll();
    }

    /**
     * 有効なビール一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Item> getActiveItems() {
        return ItemRepository.findByIsActiveTrueOrderByName();
    }

    /**
     * IDでビールを取得
     */
    @Transactional(readOnly = true)
    public Optional<Item> getItemById(Long id) {
        return ItemRepository.findById(id);
    }

    /**
     * ビールを新規登録
     */
    public Item saveItem(Item item) {
        if (item.getShelfLifeDay() == null) {
            item.setShelfLifeDay(15);
        }
        if (item.getPrice() != null && item.getPrice() <= 0) {
            throw new IllegalArgumentException("価格は1円以上である必要があります");
        }
        if (item.getShelfLifeDay() != null && item.getShelfLifeDay() < 1) {
            throw new IllegalArgumentException("賞味期限は1日以上である必要があります");
        }
        return ItemRepository.save(item);
    }

    /**
     * ビールを論理削除
     */
    public void deactivateItem(Long id) {
        Optional<Item> itemOpt = ItemRepository.findById(id);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            item.setIsActive(false);
            ItemRepository.save(item);
        } else {
            throw new IllegalArgumentException("ビールが見つかりません: " + id);
        }
    }
    

    /**
     * ビールを物理削除
     */
    public void deleteItem(Long id) {
        if (!ItemRepository.existsById(id)) {
            throw new IllegalArgumentException("ビールが見つかりません: " + id);
        }
        ItemRepository.deleteById(id);
    }

    /**
     * ビールの更新
     */
    public Item updateItem(Long id, Item updatedItem) {
        Item existingItem = ItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ビールが見つかりません: " + id));

        // 値が設定されている場合のみ更新
        if (updatedItem.getName() != null) {
            existingItem.setName(updatedItem.getName());
        }
        if (updatedItem.getPrice() != null) {
            existingItem.setPrice(updatedItem.getPrice());
        }
        if (updatedItem.getShelfLifeDay() != null) {
            existingItem.setShelfLifeDay(updatedItem.getShelfLifeDay());
        }
        if (updatedItem.getDescription() != null) {
            existingItem.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getIsActive() != null) {
            existingItem.setIsActive(updatedItem.getIsActive());
        }
        return ItemRepository.save(existingItem);
    }
}
