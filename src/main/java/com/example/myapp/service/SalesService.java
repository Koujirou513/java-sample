package com.example.myapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.dto.SalesInputDto;
import com.example.myapp.entity.Sales;
import com.example.myapp.entity.SalesItems;
import com.example.myapp.entity.User;
import com.example.myapp.repository.SalesItemsRepository;
import com.example.myapp.repository.SalesRepository;

@Service
@Transactional
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SalesItemsRepository salesItemsRepository;

    @Autowired
    private ItemService itemService;

    /**
     * 販売実績を新規登録または更新
     */
    public Sales createOrUpdateSales(Sales sales) {
        return salesRepository.save(sales);
    }
    /**
     * 販売実績をIDで取得
     */    
    public Optional<Sales> findByTargetDate(LocalDate date) {
        return salesRepository.findByDate(date);
    }
    /**
     * 全ての販売実績を取得
     */
    public List<Sales> gatAllSales() {
        return salesRepository.findAllOrderByDateDesc();
    }

    public Integer getTotalSalesQuantityByDate(LocalDate date) {
        return salesItemsRepository.sumQuantityByDate(date);
    }

    public Sales saveSalesData(SalesInputDto salesInput, User currentUser) {
        Sales sales = new Sales();
        sales.setDate(salesInput.getTargetDate());
        sales.setWeather(salesInput.getWeather());
        sales.setCreatedBy(currentUser);

        // 販売アイテムも保存
        List<SalesItems> items = new ArrayList<>();
        for (SalesInputDto.SalesItemDto itemDto : salesInput.getSalesItems()) {
            if (itemDto.getQuantity() > 0) {
                SalesItems item = new SalesItems();
                item.setSales(sales);
                item.setItem(itemService.findById(itemDto.getItemId()).orElse(null));
                item.setQuantity(itemDto.getQuantity());
                items.add(item);
            }
        }
        sales.setSalesItems(items);

        return salesRepository.save(sales);
    }

}
