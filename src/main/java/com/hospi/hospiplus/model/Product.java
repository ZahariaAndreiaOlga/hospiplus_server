package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Table("Product")
public class Product {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("code")
    @NotNull(message = "Code must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private String code;

    @Column("mu")
    @NotNull(message = "MU must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private String mu;

    @Column("quantity")
    @NotNull(message = "Quantity must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer quantity;

    @Column("criticalQuantity")
    @NotNull(message = "Critical quantity must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer criticalQuantity;

    @Column("pricePerUnit")
    @NotNull(message = "Price per unit must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private Float pricePerUnit;

    @Column("createdAt")
    @JsonView(Views.Output.class)
    private LocalDateTime createdAt;

    @Column("updatedAt")
    @JsonView(Views.Output.class)
    private LocalDateTime updatedAt;

    @Column("categoryId")
    private Integer categoryId;

    @MappedCollection(idColumn = "productId")
    @JsonView(Views.Output.class)
    private Set<ProductHistory> productRecords;

    public Product() {}

    public Product(String code, String mu, Integer quantity, Integer criticalQuantity, Float pricePerUnit) {
        this.code = code;
        this.mu = mu;
        this.quantity = quantity;
        this.criticalQuantity = criticalQuantity;
        this.pricePerUnit = pricePerUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCriticalQuantity() {
        return criticalQuantity;
    }

    public void setCriticalQuantity(Integer criticalQuantity) {
        this.criticalQuantity = criticalQuantity;
    }

    public Float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Set<ProductHistory> getProductRecords() {
        return productRecords;
    }

    public void setProductRecords(Set<ProductHistory> productRecords) {
        this.productRecords = productRecords;
    }

    public void addProductRecords(ProductHistory productHistory){
        productRecords.add(productHistory);
    }

    public void removeProductRecords(ProductHistory productHistory){
        productRecords.remove(productHistory);
    }
}
