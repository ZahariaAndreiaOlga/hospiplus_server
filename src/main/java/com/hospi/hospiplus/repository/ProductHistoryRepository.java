package com.hospi.hospiplus.repository;

import com.hospi.hospiplus.model.ProductHistory;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductHistoryRepository extends CrudRepository<ProductHistory, Integer> {

    @Query("SELECT * FROM `ProductHistory` WHERE productId = :productId")
    List<ProductHistory> findHistoriesByProductId(Integer productId);

}
