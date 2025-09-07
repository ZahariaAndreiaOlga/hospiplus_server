package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.model.ProductWardHistory;
import com.hospi.hospiplus.repository.ProductWardHistoryRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/product-ward-history")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class ProductWardHistoryController {

    private final ProductWardHistoryRepository repository;

    public ProductWardHistoryController(ProductWardHistoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<ProductWardHistory> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public ProductWardHistory findOne(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductWardHistory not found"));
    }

    @PostMapping
    public ResponseEntity<ProductWardHistory> create(
            @Valid @RequestBody @JsonView(Views.Input.class) ProductWardHistory body) {
        repository.save(body);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductWardHistory> update(
            @PathVariable Integer id,
            @Valid @RequestBody @JsonView(Views.Input.class) ProductWardHistory body) {

        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductWardHistory not found");
        }
        body.setId(id);
        repository.save(body);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductWardHistory not found");
        }
        repository.deleteById(id);
    }
}
