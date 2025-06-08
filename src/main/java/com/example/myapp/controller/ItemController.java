package com.example.myapp.controller;

import com.example.myapp.entity.Item;
import com.example.myapp.entity.User;
import com.example.myapp.service.ItemService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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
        model.addAttribute("editMode", false);
        return "items/form";
    }
    
    /*
     * 商品追加処理（管理者のみ）
     */
    @PostMapping("/add")
    public String addItem(@Valid @ModelAttribute Item item,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal User currentUser,
                        RedirectAttributes redirectAttributes) {

        if (!currentUser.getIsAdmin()) {
            return "redirect:/items?error=permission";
        }
        if (result.hasErrors()) {
            model.addAttribute("editMode", false);
            return "items/form";
        }

        try {
            // 新規作成時はデフォルトでアクティブに設定
            item.setIsActive(true);
            itemService.createItem(item);
            redirectAttributes.addAttribute("successMessage", "商品が正常に追加されました。");
            return "redirect:/items";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "商品の追加に失敗しました: " + e.getMessage());
            model.addAttribute("editMode", false);
            return "items/form";
        }
    }

    /*
     * 商品編集ページを表示（管理者のみ）
     */
    @GetMapping("/edit/{id}")
    public String editItemFrom(@PathVariable Long id,
                            Model model,
                            @AuthenticationPrincipal User currentUser) {
        
        if (!currentUser.getIsAdmin()) {
            return "redirect:/items?error=permission";
        }

        Optional<Item> itemOpt = itemService.findById(id);
        if (itemOpt.isEmpty()) {
            return "redirect:/item?error=not_found";
        }

        model.addAttribute("item", itemOpt.get());
        model.addAttribute("editMode", true);
        return "items/form";
    }

    /*
     * 商品編集処理（管理者のみ）
     */
    @PostMapping("/edit/{id}")
    public String editItem(@PathVariable Long id,
                        @Valid @ModelAttribute Item item,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal User currentUser,
                        RedirectAttributes redirectAttributes) {
        
        if (!currentUser.getIsAdmin()) {
            return "redirect:/item?error=permission";
        }

        if (result.hasErrors()) {
            model.addAttribute("editMode", true);
            return "items/form";
        } 

        try {
            item.setId(id);
            itemService.updateItem(id, item);
            redirectAttributes.addFlashAttribute("successMessage", "商品が正常に更新されました。");
            return "redirect:/items";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "商品の更新に失敗しました: " + e.getMessage());
            model.addAttribute("editMode", true);
            return "item/form";
        }
    }

        /*
         * 商品の有効/無効を切り替え（管理者のみ）
         */
        @PostMapping("/toggle/{id}")
        public String toggleItemStatus(@PathVariable Long id,
                                    @AuthenticationPrincipal User currentUser,
                                    RedirectAttributes redirectAttributes) {
            
            if (!currentUser.getIsAdmin()) {
                return "redirect:/items?error=permission";
            }

            try {
                itemService.toggleItemStatus(id);
                redirectAttributes.addFlashAttribute("successMessage", "商品のステータスが更新されました");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "ステータスの変更に失敗しました: " + e.getMessage()); 
            }

            return "redirect:/items";
        }
}
