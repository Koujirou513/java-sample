package com.example.myapp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.dto.SalesDisplayDto;
import com.example.myapp.dto.SalesInputDto;
import com.example.myapp.entity.Item;
import com.example.myapp.entity.Sales;
import com.example.myapp.entity.User;
import com.example.myapp.service.ItemService;
import com.example.myapp.service.SalesService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private SalesService salesService;

    @GetMapping
    public String salesList(Model model, @AuthenticationPrincipal User currentUser) {
        List<SalesDisplayDto> salesList = salesService.getSalesDisplayList();
        model.addAttribute("salesList", salesList);
        model.addAttribute("message", "販売実績一覧ページ");
        return "sales/list"; 
    }

    @GetMapping("/input")
    public String salesInput(Model model, @AuthenticationPrincipal User currentUser) {

        LocalDate today = LocalDate.now();

        System.out.println("=== 新規登録デバッグ ===");
        System.out.println("Today: " + today);

        Optional<Sales> todaySales = salesService.findByDate(today);

        if (todaySales.isPresent()) {
            return "redirect:/dashboard?error=already_exists";
        }

        SalesInputDto salesInputDto = new SalesInputDto();
        salesInputDto.setDate(today);

        List<Item> activeItems = itemService.getActiveItems();
        salesInputDto.initializeWithAllItems(activeItems);

        model.addAttribute("salesInput", salesInputDto);
        model.addAttribute("items", activeItems);
        model.addAttribute("editMode", false);
        model.addAttribute("message", "販売実績入力ページ");
        return "sales/input"; 
    }

    @PostMapping("/input")
    public String salesInputPost(@Valid @ModelAttribute SalesInputDto salesInput,
                                BindingResult result,
                                Model model,
                                @AuthenticationPrincipal User currentUser,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // バリデーションエラーがある場合は再表示
            salesInput.setDate(LocalDate.now());
            model.addAttribute("items", itemService.getActiveItems());
            model.addAttribute("editMode", false);
            return "sales/input";
        }

        try {
            // 販売データを保存するSalesServiceを呼び出す
            salesService.saveSalesData(salesInput, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "販売データが正常に保存されました");
            return "redirect:/sales";
        } catch (Exception e) {
            salesInput.setDate(LocalDate.now());
            model.addAttribute("error", "販売データの保存に失敗しました: " + e.getMessage());
            model.addAttribute("editMode", false);
            model.addAttribute("items", itemService.getActiveItems());
            return "sales/input";
        }
    }

    /*
     * 販売実績編集ページを表示
     */
    @GetMapping("/edit/{id}")
    public String salesEdit(@PathVariable Long id,
                            Model model,
                            @AuthenticationPrincipal User currentUser) {

        try {
            System.out.println("=== 編集デバッグ ===");
            System.out.println("Editing sales ID: " + id);
            // 既存の販売実績を取得
            SalesInputDto salesInputDto = salesService.getSalesForEdit(id);
            
            System.out.println("DTO from service - date: " + salesInputDto.getDate());
            System.out.println("DTO from service - weather: " + salesInputDto.getWeather());
            System.out.println("DTO from service - salesItems size: " + 
                (salesInputDto.getSalesItems() != null ? salesInputDto.getSalesItems().size() : "null"));

            model.addAttribute("salesInput", salesInputDto);
            model.addAttribute("items", itemService.getActiveItems());
            model.addAttribute("editMode", true);
            model.addAttribute("salesId", id);
            model.addAttribute("message", "販売実績編集ページ");

            return "sales/input";

        } catch (RuntimeException e) {
            return "redirect:/sales?error=not_found";
        }
    }

    /*
     * 販売実績の更新処理
     */
    @PostMapping("/edit/{id}")
    public String salesEditPost(@PathVariable Long id,
                                @Valid @ModelAttribute SalesInputDto salesInput,
                                BindingResult result,
                                Model model,
                                @AuthenticationPrincipal User currentUser,
                                RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("items", itemService.getActiveItems());
            model.addAttribute("editMode", true);
            model.addAttribute("salesId", id);
            return "sales/input";
        }

        try {
            // 販売データを更新
            salesService.updateSalesData(id, salesInput, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "販売データが正常に更新されました");
            return "redirect:/sales";
            
        } catch (RuntimeException e) {
            model.addAttribute("error", "販売でデータの更新に失敗しました: " + e.getMessage());
            model.addAttribute("items", itemService.getActiveItems());
            model.addAttribute("editMode", true);
            model.addAttribute("salesId", id);
            return "sales/input"; 
        }
    }
}
