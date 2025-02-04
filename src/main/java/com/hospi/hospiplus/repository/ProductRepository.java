package com.hospi.hospiplus.repository;

import com.hospi.hospiplus.model.Product;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("SELECT * FROM Product WHERE categoryId = :categoryId")
    List<Product> findProductsByCategoryId(Integer categoryId);

}
