package com.example.myapp.repository;

import com.example.myapp.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    /**
     * 特定の日付の販売実績を取得
     */
    Optional<Sales> findByDate(LocalDate date);
    
    /**
     * 全ての販売実績を降順で取得
     */
    @Query("SELECT s FROM Sales s ORDER BY s.date DESC")
    List<Sales> findAllOrderByDateDesc();
}
