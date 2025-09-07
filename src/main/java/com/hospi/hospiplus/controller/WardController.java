package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.model.Ward;
import com.hospi.hospiplus.repository.WardRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/ward")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class WardController {

    private final WardRepository wardRepository;

    public WardController(WardRepository wardRepository){
        this.wardRepository = wardRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<Ward> findWards() {return wardRepository.findAll(); }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public Ward findWard(@PathVariable Integer id){
        return wardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ward is not found"));
    }

    @PostMapping
    @JsonView(Views.Output.class)
    public ResponseEntity<Ward> createWard(@Valid @RequestBody @JsonView(Views.Input.class) Ward ward){
        wardRepository.save(ward);
        return ResponseEntity.ok(ward);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Output.class)
    public ResponseEntity<Ward> updateWard(@PathVariable Integer id, @Valid @RequestBody @JsonView(Views.Input.class) Ward ward){

        if(!wardRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ward not found");
        }
        ward.setId(id);

        wardRepository.save(ward);
        return ResponseEntity.ok(ward);
    }

    @DeleteMapping("/{id}")
    public void deleteWard(@PathVariable Integer id){
        if(!wardRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ward not found");
        }
        wardRepository.deleteById(id);
    }

}