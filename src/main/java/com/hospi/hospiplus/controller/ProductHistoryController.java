package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.ProductHistory;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.repository.ProductHistoryRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/producthistory")
@Validated
@PreAuthorize("hasRole('ADMIN') or hasRole('ECO-DEP')")
@SecurityRequirement(name = "bearerAuth")
public class ProductHistoryController {

    private final ProductHistoryRepository productHistoryRepository;

    public ProductHistoryController(ProductHistoryRepository productHistoryRepository) {
        this.productHistoryRepository = productHistoryRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<ProductHistory> findProductRecords(){
        return productHistoryRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public ProductHistory findProductHistory(@PathVariable Integer id){
        return productHistoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product History not found"));
    }

    @PostMapping
    @JsonView(Views.Output.class)
    public ResponseEntity<ProductHistory> createProductHistory(@Valid @RequestBody @JsonView(Views.Input.class) ProductHistory productHistory){
        LocalDateTime dtn = LocalDateTime.now();
        productHistory.setCreatedAt(dtn);
        productHistoryRepository.save(productHistory);
        return ResponseEntity.ok(productHistory);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Output.class)
    public ResponseEntity<ProductHistory> updateProductHistory(@PathVariable Integer id, @Valid @RequestBody @JsonView(Views.Input.class) ProductHistory productHistory){

        ProductHistory productHistoryDb = productHistoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product History not found"));
        productHistoryDb.setQuantity(productHistory.getQuantity());
        productHistoryRepository.save(productHistoryDb);
        return ResponseEntity.ok(productHistoryDb);
    }

    @DeleteMapping("/{id}")
    public void deleteProductHistory(@PathVariable Integer id){
        if(!productHistoryRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product History not found");
        }
        productHistoryRepository.deleteById(id);
    }

}
