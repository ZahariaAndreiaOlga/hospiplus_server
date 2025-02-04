package com.hospi.hospiplus.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Table("Order")
public class Order {

    @Id
    private Integer id;

    @Column("orderNum")
    @NotNull
    private Integer orderNum;

    @Column("orderedAt")
    @NotNull
    private LocalDateTime orderedAt;

    @Column("arrival")
    @NotNull
    private LocalDate arrival;

    @Column("quantity")
    @NotNull
    private Integer quantity;

    @Column("pricePerProduct")
    @NotNull
    private Float pricePerProduct;

    @Column("totalPrice")
    @NotNull
    private Float totalPrice;

    @Column("userId")
    @NotNull
    private Integer userId;

    @MappedCollection(idColumn = "orderId")
    private Set<OrderedProduct> products;

    public Order() {}

    public Order(Integer orderNum, LocalDateTime orderedAt, LocalDate arrival, Integer quantity, Float pricePerProduct, Float totalPrice) {
        this.orderNum = orderNum;
        this.orderedAt = orderedAt;
        this.arrival = arrival;
        this.quantity = quantity;
        this.pricePerProduct = pricePerProduct;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPricePerProduct() {
        return pricePerProduct;
    }

    public void setPricePerProduct(Float pricePerProduct) {
        this.pricePerProduct = pricePerProduct;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Set<OrderedProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<OrderedProduct> products) {
        this.products = products;
    }

    public void addProduct(OrderedProduct orderedProduct){
        this.products.add(orderedProduct);
    }

    public void removeProduct(OrderedProduct orderedProduct){
        this.products.remove(orderedProduct);
    }

}

