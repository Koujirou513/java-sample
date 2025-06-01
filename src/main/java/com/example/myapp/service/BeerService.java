package com.example.myapp.service;

import com.example.myapp.entity.Beer;
import com.example.myapp.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BeerService {
    
    @Autowired
    private BeerRepository beerRepository;

    /**
     * 全ビール一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    /**
     * 有効なビール一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Beer> getActivBeers() {
        return beerRepository.findByIsActiveTrueOrderByName();
    }

    /**
     * IDでビールを取得
     */
    @Transactional(readOnly = true)
    public Optional<Beer> getBeerById(Long id) {
        return beerRepository.findById(id);
    }

    /**
     * ビールを新規登録
     */
    public Beer saveBeer(Beer beer) {
        if (beer.getShelfLifeDay() == null) {
            beer.setShelfLifeDay(15);
        }
        if (beer.getPrice() != null && beer.getPrice() <= 0) {
            throw new IllegalArgumentException("価格は1円以上である必要があります");
        }
        if (beer.getShelfLifeDay() != null && beer.getShelfLifeDay() < 1) {
            throw new IllegalArgumentException("賞味期限は1日以上である必要があります");
        }
        return beerRepository.save(beer);
    }

    /**
     * ビールを論理削除
     */
    public void deactivateBeer(Long id) {
        Optional<Beer> beerOpt = beerRepository.findById(id);
        if (beerOpt.isPresent()) {
            Beer beer = beerOpt.get();
            beer.setIsActive(false);
            beerRepository.save(beer);
        } else {
            throw new IllegalArgumentException("ビールが見つかりません: " + id);
        }
    }
    

    /**
     * ビールを物理削除
     */
    public void deleteBeer(Long id) {
        if (!beerRepository.existsById(id)) {
            throw new IllegalArgumentException("ビールが見つかりません: " + id);
        }
        beerRepository.deleteById(id);
    }

    /**
     * ビールの更新
     */
    public Beer updateBeer(Long id, Beer updatedBeer) {
        Beer existingBeer = beerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ビールが見つかりません: " + id));

        // 値が設定されている場合のみ更新
        if (updatedBeer.getName() != null) {
            existingBeer.setName(updatedBeer.getName());
        }
        if (updatedBeer.getPrice() != null) {
            existingBeer.setPrice(updatedBeer.getPrice());
        }
        if (updatedBeer.getShelfLifeDay() != null) {
            existingBeer.setShelfLifeDay(updatedBeer.getShelfLifeDay());
        }
        if (updatedBeer.getDescription() != null) {
            existingBeer.setDescription(updatedBeer.getDescription());
        }
        if (updatedBeer.getIsActive() != null) {
            existingBeer.setIsActive(updatedBeer.getIsActive());
        }
        return beerRepository.save(existingBeer);
    }
}
