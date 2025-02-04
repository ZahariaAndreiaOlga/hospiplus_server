package com.hospi.hospiplus.repository;

import com.hospi.hospiplus.model.DTO.OrderDetailsDTO;
import com.hospi.hospiplus.model.Order;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Query("SELECT * FROM `Order` WHERE userId = :userId")
    List<Order> findOrderByUserId(Integer userId);

    @Query("SELECT Product.code as code, Product.mu as mu, OrderedProduct.quantity as quantity, OrderedProduct.pricePerUnit as price, OrderedProduct.totalPrice as total " +
            "FROM OrderedProduct " +
            "INNER JOIN Product " +
            "ON OrderedProduct.productId = Product.id " +
            "WHERE OrderedProduct.orderId = :orderId")
    List<OrderDetailsDTO> findOrderDetailsByOrderId(Integer orderId);

}
