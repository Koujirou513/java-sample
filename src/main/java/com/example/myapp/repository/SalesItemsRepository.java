package com.example.myapp.repository;

import com.example.myapp.entity.SalesItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemsRepository extends JpaRepository<SalesItems, Long> {

}
