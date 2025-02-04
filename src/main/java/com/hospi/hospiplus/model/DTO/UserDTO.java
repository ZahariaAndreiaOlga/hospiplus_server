package com.hospi.hospiplus.model.DTO;

import com.hospi.hospiplus.model.User;

import java.time.LocalDateTime;

public class UserDTO {

    private Integer id;
    private String userName;
    private String surname;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer roleId;

    public UserDTO() {}

    public UserDTO(User user){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.roleId = user.getRoleId();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
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
}
