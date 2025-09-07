package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("PharmacyWardTransfer")
public class PharmacyWardTransfer {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("productID")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer productId;

    @Column("wardID")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer wardId;

    @Column("quantity")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer quantity;

    @Column("typeOfTransfer")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private String typeOfTransfer;
    /*
    @MappedCollection(idColumn = "id")
    private Set<Product> products;

    @MappedCollection(idColumn = "id")
    private Set<Ward> wards;
    */
    public PharmacyWardTransfer() {}

    public PharmacyWardTransfer(Integer productId, Integer wardId, Integer quantity, String typeOfTransfer) {
        this.productId = productId;
        this.wardId = wardId;
        this.quantity = quantity;
        this.typeOfTransfer = typeOfTransfer;
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

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTypeOfTransfer() {
        return typeOfTransfer;
    }

    public void setTypeOfTransfer(String typeOfTransfer) {
        this.typeOfTransfer = typeOfTransfer;
    }

    /*
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<Ward> getWards() {
        return wards;
    }

    public void setWards(Set<Ward> wards) {
        this.wards = wards;
    }
    */
}
