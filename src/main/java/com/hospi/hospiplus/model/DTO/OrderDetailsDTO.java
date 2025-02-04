package com.hospi.hospiplus.model.DTO;

public class OrderDetailsDTO {

    private String code;
    private String mu;
    private Integer quantity;
    private Float price;
    private Float total;

    public OrderDetailsDTO() {}

    public OrderDetailsDTO(String code, String mu, Integer quantity, Float price, Float total) {
        this.code = code;
        this.mu = mu;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}
