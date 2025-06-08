package com.example.myapp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myapp.entity.Sales;
import com.example.myapp.entity.User;
import com.example.myapp.service.ItemService;
import com.example.myapp.service.SalesService;

@Controller
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private SalesService salesService;

    @Autowired
    private ItemService itemService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User currentUser) {

        LocalDate today = LocalDate.now();
        // デバッグ用ログ
        System.out.println("=== ダッシュボード デバッグ情報 ===");
        System.out.println("今日の日付: " + today);

        Optional<Sales> todaysSales = salesService.findByDate(today);
        boolean hasTodaySales = todaysSales.isPresent();

        log.info("今日の販売実績: {}", hasTodaySales ? "あり" : "なし");
        if (hasTodaySales) {
            Sales sales = todaysSales.get();
            log.info("販売実績ID: {}", sales.getId());
            log.info("販売日: {}", sales.getDate());
            log.info("天気: {}", sales.getWeather());
        }
        List<Sales> allSales = salesService.getAllSales();
        log.info("全販売実績数: {}", allSales.size());
        for (Sales sales : allSales) {
            log.info("- 日付: {}, ID: {}", sales.getDate(), sales.getId());
        }
        log.info("=========================");
        
        model.addAttribute("hasTodaySales", hasTodaySales);

        long totalItems = itemService.getActiveItemsCount();
        model.addAttribute("totalItems", totalItems);

        model.addAttribute("user", currentUser);
        return "dashboard/index";
    }
}
