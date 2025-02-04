package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("ProductHistory")
public class ProductHistory {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("quantity")
    @NotNull(message = "Quantity must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer quantity;

    @Column("createdAt")
    @JsonView(Views.Output.class)
    private LocalDateTime createdAt;

    @Column("productId")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer productId;

    public ProductHistory() {}

    public ProductHistory(Integer quantity){
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
