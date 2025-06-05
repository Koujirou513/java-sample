package com.example.myapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.entity.Sales;
import com.example.myapp.repository.SalesItemsRepository;
import com.example.myapp.repository.SalesRepository;

@Service
@Transactional
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SalesItemsRepository salesItemsRepository;

    /**
     * 販売実績を新規登録または更新
     */
    public Sales createOrUpdateSales(Sales sales) {
        return salesRepository.save(sales);
    }
    /**
     * 販売実績をIDで取得
     */    
    public Optional<Sales> findByTargetDate(LocalDate targetDate) {
        return salesRepository.findByTargetDate(targetDate);
    }
    /**
     * 全ての販売実績を取得
     */
    public List<Sales> gatAllSales() {
        return salesRepository.findAllOrderByTargetDateDesc();
    }

    public Integer getTotalSalesQuantityByDate(LocalDate targetDate) {
        return salesItemsRepository.sumQuantityByTargetDate(targetDate);
    }

}
