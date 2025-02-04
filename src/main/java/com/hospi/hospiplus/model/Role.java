package com.hospi.hospiplus.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Role")
public class Role {

    @Id
    @JsonView(Views.Output.class)
    private Integer id;

    @Column("roleName")
    @NotNull(message = "Role must not be null")
    @Size(min = 3, max = 50, message = "Role must be between 3 and 50 characters")
    @JsonView({Views.Input.class, Views.Output.class})
    private String roleName;

    public Role() {}

    public Role(String roleName){
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
