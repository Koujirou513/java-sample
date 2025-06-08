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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.dto.SalesDisplayDto;
import com.example.myapp.dto.SalesInputDto;
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
        Optional<Sales> todaySales = salesService.findByDate(today);
        boolean hasTodaySales = todaySales.isPresent();

        if (hasTodaySales) {
            return "redirect:/dashboard?error=already_exists";
        }

        model.addAttribute("salesInput", new SalesInputDto());
        model.addAttribute("items", itemService.getActiveItems());
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
            model.addAttribute("items", itemService.getActiveItems());
            return "sales/input";
        }
        try {
            // 販売データを保存するSalesServiceを呼び出す
            salesService.saveSalesData(salesInput, currentUser);

            redirectAttributes.addFlashAttribute("successMessage", "販売データが正常に保存されました");

            return "redirect:/sales?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "販売データの保存に失敗しました: " + e.getMessage());
            model.addAttribute("items", itemService.getActiveItems());
            return "sales/input";
        }
    }
}
