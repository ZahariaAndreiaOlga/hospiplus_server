package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Table("Category")
public class Category {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("catgName")
    @NotNull(message = "Name must not be null")
    @Size(min = 3, max = 50, message = "Category must be between 3 and 50 characters")
    @JsonView({Views.Input.class, Views.Output.class})
    private String catgName;

    @Column("createdAt")
    @JsonView(Views.Output.class)
    private LocalDateTime createdAt;

    @Column("updatedAt")
    @JsonView(Views.Output.class)
    private LocalDateTime updatedAt;

    @MappedCollection(idColumn = "categoryId")
    @JsonView(Views.Output.class)
    private Set<Product> products;

    public Category() {}

    public Category(String catgName){
        this.catgName = catgName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCatgName() {
        return catgName;
    }

    public void setCatgName(String catgName) {
        this.catgName = catgName;
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
    }
}
