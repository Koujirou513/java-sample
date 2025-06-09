package com.example.myapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myapp.dto.SalesDisplayDto;
import com.example.myapp.dto.SalesInputDto;
import com.example.myapp.entity.Item;
import com.example.myapp.entity.Sales;
import com.example.myapp.entity.SalesItems;
import com.example.myapp.entity.User;
import com.example.myapp.enums.Weather;
import com.example.myapp.repository.ItemRepository;
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

    @Autowired
    private ItemRepository itemRepository;

    /**
     * 販売実績を新規登録または更新
     */
    public Sales createOrUpdateSales(Sales sales) {
        return salesRepository.save(sales);
    }
    /**
     * 販売実績をIDで取得
     */    
    public Optional<Sales> findByDate(LocalDate date) {
        return salesRepository.findByDate(date);
    }
    /**
     * 全ての販売実績を取得
     */
    public List<Sales> getAllSales() {
        return salesRepository.findAllOrderByDateDesc();
    }

    public Integer getTotalSalesQuantityByDate(LocalDate date) {
        return salesItemsRepository.sumQuantityByDate(date);
    }

    public Sales saveSalesData(SalesInputDto salesInput, User currentUser) {
        Sales sales = new Sales();
        sales.setDate(salesInput.getDate());
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

    public List<SalesDisplayDto> getSalesDisplayList() {
        List<Sales> salesList = getAllSales();
        List<SalesDisplayDto> displayList = new ArrayList<>();

        for (Sales sales : salesList) {
            SalesDisplayDto displayDto = new SalesDisplayDto();
            displayDto.setId(sales.getId());
            displayDto.setDate(sales.getDate());
            displayDto.setCreatedByName(sales.getCreatedBy().getName());
            displayDto.setWeather(sales.getWeather());

            displayDto.setWeatherDisplay(Weather.getDisplayTextByCode(sales.getWeather()));

            List<SalesDisplayDto.SalesItemDisplayDto> itemDisplayList = new ArrayList<>();
            int totalQuantity = 0;

            for (SalesItems item : sales.getSalesItems()) {
                SalesDisplayDto.SalesItemDisplayDto itemDisplay = new SalesDisplayDto.SalesItemDisplayDto();
                itemDisplay.setItemName(item.getItem().getName());
                itemDisplay.setQuantity(item.getQuantity());
                itemDisplayList.add(itemDisplay);
                totalQuantity += item.getQuantity();
            }

            displayDto.setSalesItems(itemDisplayList);
            displayDto.setTotalQuantity(totalQuantity);
            displayList.add(displayDto);
        }
        return displayList;
    }

    /*
     * 販売実績編集
     */
    @Transactional
    public SalesInputDto getSalesForEdit(Long salesId) {
        Sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new RuntimeException("販売実績が見つかりません: " + salesId));

        System.out.println("Found sales - ID: " + sales.getId());
        System.out.println("Found sales - Date: " + sales.getDate());
        System.out.println("Found sales - Weather: " + sales.getWeather());

        // EntityからDTOに変換
        SalesInputDto dto = new SalesInputDto();
        dto.setDate(sales.getDate());
        dto.setWeather(sales.getWeather());

        System.out.println("DTO after setting basic fields:");
        System.out.println("  - date: " + dto.getDate());
        System.out.println("  - weather: " + dto.getWeather());

        // 販売アイテムの情報をDTOに設定
        List<SalesItems> salesItems = salesItemsRepository.findBySalesId(salesId);

        // 各商品の販売数量をDTOに変換
        List<SalesInputDto.SalesItemDto> salesItemDtoList = new ArrayList<>();
        for (SalesItems salesItem : salesItems) {
            SalesInputDto.SalesItemDto itemDto = new SalesInputDto.SalesItemDto();
            itemDto.setItemId(salesItem.getItem().getId());
            itemDto.setQuantity(salesItem.getQuantity());
            salesItemDtoList.add(itemDto);
        }
        dto.setSalesItems(salesItemDtoList);

        System.out.println("Final DTO:");
        System.out.println("  - date: " + dto.getDate());
        System.out.println("  - weather: " + dto.getWeather());
        System.out.println("  - salesItems size: " + dto.getSalesItems().size());
        System.out.println("=== getSalesForEdit 終了 ===");

        return dto;
    }

    /*
     * 販売実績を更新
     */
    public void updateSalesData(Long salesId, SalesInputDto salesInputDto, User updateBy) {
        Sales existingSales = salesRepository.findById(salesId)
                .orElseThrow(() -> new RuntimeException("販売実績が見つかりません: " + salesId));

        existingSales.setDate(salesInputDto.getDate());
        existingSales.setWeather(salesInputDto.getWeather());
        existingSales.setUpdatedAt(LocalDateTime.now());

        salesItemsRepository.deleteBySalesId(salesId);

        List<SalesItems> newSalesItems = new ArrayList<>(); 

        for (SalesInputDto.SalesItemDto itemDto : salesInputDto.getSalesItems()) {
            if (itemDto.getQuantity() != null && itemDto.getQuantity() > 0) {
                Item item = itemRepository.findById(itemDto.getItemId())
                        .orElseThrow(() -> new RuntimeException("商品が見つかりません: " + itemDto.getItemId()));

                SalesItems salesItem = new SalesItems();
                salesItem.setSales(existingSales);
                salesItem.setItem(item);
                salesItem.setQuantity(itemDto.getQuantity());
                
                newSalesItems.add(salesItem);
            }
        }
        salesItemsRepository.saveAll(newSalesItems);

        salesRepository.save(existingSales);
    }
}
