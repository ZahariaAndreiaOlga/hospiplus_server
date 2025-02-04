package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.Role;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.repository.RoleRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/role")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<Role> findRoles(){
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public Role findRole(@PathVariable Integer id){
        return roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    @PostMapping
    @JsonView(Views.Output.class)
    public ResponseEntity<Role> createRole(@Valid @RequestBody  @JsonView(Views.Input.class) Role role){
        roleRepository.save(role);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Output.class)
    public ResponseEntity<Role> updateRole(@PathVariable Integer id, @Valid @RequestBody @JsonView(Views.Input.class) Role role){
        if(!roleRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        role.setId(id);
        roleRepository.save(role);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Integer id){
        if(!roleRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        roleRepository.deleteById(id);
    }

}
