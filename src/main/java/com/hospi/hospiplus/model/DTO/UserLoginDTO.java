package com.hospi.hospiplus.model.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserLoginDTO {

    @NotNull(message = "Email must not be null")
    private String email;
    @NotNull(message = "Password must not be null")
    @Size(min = 5, max = 50, message = "Password must be between 5 and 50 characters")
    private String password;

    public UserLoginDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
