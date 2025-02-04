package com.hospi.hospiplus.model.DTO;

import jakarta.validation.constraints.NotNull;

public class BasketOrdered {

    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;

    public BasketOrdered() {}

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
