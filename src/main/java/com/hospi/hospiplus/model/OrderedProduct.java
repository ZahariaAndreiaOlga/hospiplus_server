package com.hospi.hospiplus.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("OrderedProduct")
public class OrderedProduct {

    @Id
    private Integer id;

    @Column("productId")
    @NotNull(message = "Product ID must not be null")
    private Integer productId;

    @Column("orderId")
    @NotNull(message = "Order ID must not be null")
    private Integer orderId;

    @Column("quantity")
    @NotNull(message = "Quantity must not be null")
    private Integer quantity;

    @Column("pricePerUnit")
    @NotNull
    private Float pricePerUnit;

    @Column("totalPrice")
    @NotNull
    private Float totalPrice;

    public OrderedProduct() {}

    public OrderedProduct(Integer productId, Integer orderId, Integer quantity, Float pricePerUnit, Float totalPrice) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
