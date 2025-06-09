package com.example.myapp.repository;

import com.example.myapp.entity.SalesItems;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemsRepository extends JpaRepository<SalesItems, Long> {

    /**
     * 特定の日付の販売数量を取得
     */
    @Query("SELECT COALESCE(SUM(si.quantity), 0) FROM SalesItems si WHERE si.sales.date = :date")
    Integer sumQuantityByDate(LocalDate date);

    /*
     * 特定の販売IDのアイテムを取得
     */
    List<SalesItems> findBySalesId(Long salesId);

    /*
     * 特定の販売IDのアイテムを削除
     */
    void deleteBySalesId(Long salesId);
}
