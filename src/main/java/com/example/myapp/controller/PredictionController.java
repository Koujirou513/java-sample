package com.example.myapp.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myapp.entity.Item;
import com.example.myapp.service.ItemService;
import com.example.myapp.service.WeatherApiService;

@Controller
@RequestMapping("/prediction")
public class PredictionController {

    @Autowired
    private WeatherApiService weatherApiService;

    @Autowired
    private ItemService itemService;

    @GetMapping
    public String predictionPage(Model model) {
        // 次の月曜日と木曜日の発注日を計算
        LocalDate nextMonday = getNextWeekday(DayOfWeek.MONDAY);
        LocalDate nextThursday = getNextWeekday(DayOfWeek.THURSDAY);

        // 需要予測を取得
        var mondayPrediction = weatherApiService.getDemandPrediction(nextMonday);
        var thursdayPrediction = weatherApiService.getDemandPrediction(nextThursday);

        model.addAttribute("nextMonday", nextMonday);
        model.addAttribute("nextThursday", nextThursday);
        model.addAttribute("mondayPrediction", mondayPrediction);
        model.addAttribute("thursdayPrediction", thursdayPrediction);
        model.addAttribute("items", itemService.getActiveItems());

        return "prediction/index";
    }

    private LocalDate getNextWeekday(DayOfWeek targetDay) {
        LocalDate today = LocalDate.now();
        int daysToAdd = targetDay.getValue() - today.getDayOfWeek().getValue();
        if (daysToAdd <= 0) {
            daysToAdd += 7;
        }
        return today.plusDays(daysToAdd);
    }
}
