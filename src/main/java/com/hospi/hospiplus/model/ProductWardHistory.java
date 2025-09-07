package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;


@Table("ProductWardHistory")
public class ProductWardHistory {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("productWardID")
    @NotNull(message = "Product Ward ID must not be null.")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer productWardId;

    @Column("quantity")
    @NotNull(message = "Quantity must not be null.")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer quantity;

    @Column("createdAt")
    @JsonView(Views.Output.class)
    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime createdAt;

    public ProductWardHistory() {}
    public ProductWardHistory(Integer id, Integer productWardId, Integer quantity, LocalDateTime createdAt) {
        this.id = id;
        this.productWardId = productWardId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProductWardId() { return productWardId; }
    public void setProductWardId(Integer productWardId) { this.productWardId = productWardId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
