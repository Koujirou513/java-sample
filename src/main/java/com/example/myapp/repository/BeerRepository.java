package com.example.myapp.repository;

import com.example.myapp.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long>{

    // 有効なビールを取得
    List<Beer> findByIsActiveTrueOrderByName();
}
