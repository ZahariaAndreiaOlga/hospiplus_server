package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;


@Table("ProductWard")
public class ProductWard {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("wardID")
    @NotNull(message = "Ward ID must not be null.")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer wardId;

    @Column("code")
    @NotNull(message = "Code must not be null.")
    @JsonView({Views.Input.class, Views.Output.class})
    private String code;

    @Column("quantity")
    @NotNull(message = "Quantity must not be null.")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer quantity;

    @Column("criticalQuantity")
    @NotNull(message = "Critical Quantity must not be null.")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer criticalQuantity;

    @MappedCollection(idColumn = "productWardID")
    @JsonIgnore
    private Set<ProductWardHistory> productWardHistories;

    public ProductWard() {}
    public ProductWard(Integer id, Integer wardId, String code, Integer quantity, Integer criticalQuantity) {
        this.id = id;
        this.wardId = wardId;
        this.code = code;
        this.quantity = quantity;
        this.criticalQuantity = criticalQuantity;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) {this.id = id; }

    public Integer getWardId() { return wardId; }
    public void setWardId(Integer wardId) {this.wardId = wardId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getCriticalQuantity() { return criticalQuantity; }
    public void setCriticalQuantity(Integer criticalQuantity) { this.criticalQuantity = criticalQuantity; }

    public Set<ProductWardHistory> getProductWardHistories() {
        return productWardHistories;
    }

    public void setProductWardHistories(Set<ProductWardHistory> productWardHistories) {
        this.productWardHistories = productWardHistories;
    }
    /*
    public void addProductWardHistories(ProductWardHistory productWardHistory){
        this.productWardHistories.add(productWardHistory);
    }

    public void removeProductWardHistories(ProductWardHistory productWardHistory){
        this.productWardHistories.remove(productWardHistory);
    }
     */
}
