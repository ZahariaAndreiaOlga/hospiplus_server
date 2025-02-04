package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.time.LocalDateTime;
import java.util.Set;

public class User {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("userName")
    @NotNull(message = "Username must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private String userName;

    @Column("surname")
    @NotNull(message = "Surname must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private String surname;

    @Column("email")
    @NotNull(message = "Email must not be null")
    @JsonView({Views.Input.class, Views.Output.class})
    private String email;

    @Column("userPassword")
    @NotNull(message = "Password must not be null")
    @JsonView(Views.Input.class)
    private String userPassword;

    @Column("createdAt")
    @JsonView(Views.Output.class)
    private LocalDateTime createdAt;

    @Column("updatedAt")
    @JsonView(Views.Output.class)
    private LocalDateTime updatedAt;

    @Column("roleId")
    @JsonView({Views.Input.class, Views.Output.class})
    private Integer roleId;

    @MappedCollection(idColumn = "userId")
    @JsonIgnore
    private Set<Order> orders;

    public User() {}

    public User(String userName, String surname, String email, String userPassword, Integer roleId){
        this.userName = userName;
        this.surname = surname;
        this.email = email;
        this.userPassword = userPassword;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public void removeOrder(Order order){
        orders.remove(order);
    }
}
