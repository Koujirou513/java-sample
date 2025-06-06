package com.example.myapp.dto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PredictionResult {
    private LocalDate targetDate;
    private Map<Long, Integer> itemPredictions = new HashMap<>();

    public LocalDate getTargetDate() { 
        return targetDate; 
    }
    
    public void setTargetDate(LocalDate targetDate) { 
        this.targetDate = targetDate; 
    }
    
    public Map<Long, Integer> getItemPredictions() { 
        return itemPredictions; 
    }
    
    public void addItemPrediction(Long itemId, Integer quantity) { 
        itemPredictions.put(itemId, quantity); 
    }
}