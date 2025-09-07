package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("WardTransfer")
public class WardTransfer {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("idWard_giver")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer giver;

    @Column("idWard_reciever")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer reciever;

    @Column("productID")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer productId;

    @Column("quantity")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer quantity;

    @Column("transferType")
    @NotNull
    @JsonView({Views.Input.class, Views.Output.class})
    private String transferType;

    public WardTransfer() {}

    public WardTransfer(Integer giver, Integer reciever, Integer productId, Integer quantity, String transferType) {
        this.giver = giver;
        this.reciever = reciever;
        this.productId = productId;
        this.quantity = quantity;
        this.transferType = transferType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGiver() {
        return giver;
    }

    public void setGiver(Integer giver) {
        this.giver = giver;
    }

    public Integer getReciever() {
        return reciever;
    }

    public void setReciever(Integer reciever) {
        this.reciever = reciever;
    }

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

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
