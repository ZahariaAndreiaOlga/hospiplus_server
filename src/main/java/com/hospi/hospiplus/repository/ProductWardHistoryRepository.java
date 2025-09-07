package com.hospi.hospiplus.repository;

import com.hospi.hospiplus.model.ProductWardHistory;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductWardHistoryRepository extends CrudRepository<ProductWardHistory, Integer> {
    @Query("SELECT * FROM `ProductWardHistory` WHERE productWardID = :productWardID")
    Iterable<ProductWardHistory> findOrderByProductWardId(Integer productWardID);

}
