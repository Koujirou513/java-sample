package com.example.myapp.repository;

import com.example.myapp.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    /**
     * 特定の日付の販売実績を取得
     */
    Optional<Sales> findByTargetDate(LocalDate targetDate);
    
}
