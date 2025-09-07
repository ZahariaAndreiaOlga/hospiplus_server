package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("Ward")
public class Ward {
    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("wardName")
    @NotNull(message = "WardName must no be null")
    @Size(min = 3, max = 50, message = "WardName must be be between 3 and 50 characters")
    @JsonView({Views.Input.class, Views.Output.class})
    private String wardName;

    @Column("capacity")
    @NotNull(message = "Capacity mst no be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer capacity;

    @MappedCollection (idColumn = "wardID")
    @JsonView(Views.Output.class)
    private Set<ProductWard> productWards;

    //@MappedCollection(idColumn = "wardID")
    //@JsonView(Views.Output.class)
    //private Set<PharmacyWardTransfer> pharmacyWardTransfers;

    //@MappedCollection(idColumn = "idWard_giver")
    //@JsonView(Views.Output.class)
    //private Set<WardTransfer> wardTransfersGiver;

    //@MappedCollection(idColumn = "idWard_reciever")
    //@JsonView(Views.Output.class)
    //private Set<WardTransfer> wardTransfersReciever;

    public Ward() {}

    public Ward(String wardName, Integer capacity){
        this.wardName= wardName;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Set<ProductWard> getProductWards() {
        return productWards;
    }

    public void setProductWards(Set<ProductWard> productWards) {
        this.productWards = productWards;
    }

    public void addProductWards(ProductWard productWard){
        this.productWards.add(productWard);
    }

    public void removeProductWards(ProductWard productWard){
        this.productWards.remove(productWard);
    }
/*
    public Set<PharmacyWardTransfer> getPharmacyWardTransfers() {
        return pharmacyWardTransfers;
    }

    public void setPharmacyWardTransfers(Set<PharmacyWardTransfer> pharmacyWardTransfers) {
        this.pharmacyWardTransfers = pharmacyWardTransfers;
    }

    public Set<WardTransfer> getWardTransfersGiver() {
        return wardTransfersGiver;
    }

    public void setWardTransfersGiver(Set<WardTransfer> wardTransfersGiver) {
        this.wardTransfersGiver = wardTransfersGiver;
    }

    public Set<WardTransfer> getWardTransfersReciever() {
        return wardTransfersReciever;
    }

    public void setWardTransfersReciever(Set<WardTransfer> wardTransfersReciever) {
        this.wardTransfersReciever = wardTransfersReciever;
    }

    public void addPharmacyWardTransfers(PharmacyWardTransfer pharmacyWardTransfer){
        this.pharmacyWardTransfers.add(pharmacyWardTransfer);
    }

    public void removePharmacyWardTransfers(PharmacyWardTransfer pharmacyWardTransfer){
        this.pharmacyWardTransfers.remove(pharmacyWardTransfer);
    }

    public void addWardTransfersGiver(WardTransfer wardTransfer){
        this.wardTransfersGiver.add(wardTransfer);
    }

    public void removeWardTransfersGiver(WardTransfer wardTransfer){
        this.wardTransfersGiver.remove(wardTransfer);
    }

    public void addWardTransfersReciever(WardTransfer wardTransfer){
        this.wardTransfersReciever.add(wardTransfer);
    }

    public void removeWardTransfersReciever(WardTransfer wardTransfer){
        this.wardTransfersReciever.remove(wardTransfer);
    }
 */
}
