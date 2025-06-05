package com.example.myapp.repository;

import com.example.myapp.entity.SalesItems;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemsRepository extends JpaRepository<SalesItems, Long> {

    /**
     * 特定の日付の販売数量を取得
     */
    @Query("SELECT COALESCE(SUM(si.quantity), 0) FROM SalesItems si WHERE si.sales.targetDate = :targetDate")
    Integer sumQuantityByTargetDate(LocalDate targetDate);
}
