package com.hospi.hospiplus.repository;

import com.hospi.hospiplus.model.OrderedProduct;
import org.springframework.data.repository.CrudRepository;

public interface OrderedProductRepository extends CrudRepository<OrderedProduct, Integer> {
}
