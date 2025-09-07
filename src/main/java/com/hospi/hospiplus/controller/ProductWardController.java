package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.ProductWardHistory;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.model.ProductWard;
import com.hospi.hospiplus.repository.ProductWardHistoryRepository;
import com.hospi.hospiplus.repository.ProductWardRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-ward")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class ProductWardController {

    private final ProductWardRepository productWardRepository;
    private final ProductWardHistoryRepository productWardHistoryRepository;

    public ProductWardController(ProductWardRepository productWardRepository, ProductWardHistoryRepository productWardHistoryRepository) {
        this.productWardRepository = productWardRepository;
        this.productWardHistoryRepository = productWardHistoryRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<ProductWard> findProductWards() {
        return productWardRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public ProductWard findProductWard(@PathVariable Integer id) {
        return productWardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductWard is not found"));
    }

    @PostMapping
    public ResponseEntity<ProductWard> createProductWard(
            @Valid @RequestBody @JsonView(Views.Input.class) ProductWard productWard) {

        productWardRepository.save(productWard);
        return ResponseEntity.ok(productWard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductWard> updateProductWard(
            @PathVariable Integer id,
            @Valid @RequestBody @JsonView(Views.Input.class) ProductWard productWard) {

        if (!productWardRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductWard not found");
        }
        productWard.setId(id);
        productWardRepository.save(productWard);
        return ResponseEntity.ok(productWard);
    }

    @DeleteMapping("/{id}")
    public void deleteProductWard(@PathVariable Integer id) {
        if (!productWardRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductWard not found");
        }
        productWardRepository.deleteById(id);
    }

    @GetMapping("/{id}/history")
    @JsonView(Views.Output.class)
    public Iterable<ProductWardHistory> findProductWardHistoryByProductId(@PathVariable Integer id) {
        if(!productWardRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductWard not found");
        }
        return productWardHistoryRepository.findOrderByProductWardId(id);
    }
}
