package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.Product;
import com.hospi.hospiplus.model.ProductHistory;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.repository.CategoryRepository;
import com.hospi.hospiplus.repository.ProductHistoryRepository;
import com.hospi.hospiplus.repository.ProductRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Validated
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductHistoryRepository productHistoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, ProductHistoryRepository productHistoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productHistoryRepository = productHistoryRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<Product> findProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public Product findProduct(@PathVariable Integer id){
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ECO-DEP')")
    @SecurityRequirement(name = "bearerAuth")
    @JsonView(Views.Output.class)
    public ResponseEntity<Product> createProduct(@Valid @RequestBody @JsonView(Views.Input.class) Product product){

        LocalDateTime dtn = LocalDateTime.now();
        product.setCreatedAt(dtn);
        product.setUpdatedAt(dtn);
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ECO-DEP')")
    @SecurityRequirement(name = "bearerAuth")
    @JsonView(Views.Output.class)
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @Valid @RequestBody @JsonView(Views.Input.class) Product product){

        Product updateDbProduct = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        updateDbProduct.setCode(product.getCode());
        updateDbProduct.setMu(product.getMu());
        updateDbProduct.setQuantity(product.getQuantity());
        updateDbProduct.setCriticalQuantity(product.getCriticalQuantity());
        updateDbProduct.setPricePerUnit(product.getPricePerUnit());
        updateDbProduct.setCategoryId(product.getCategoryId());
        updateDbProduct.setUpdatedAt(LocalDateTime.now());

        productRepository.save(updateDbProduct);
        return ResponseEntity.ok(updateDbProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ECO-DEP')")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteProduct(@PathVariable Integer id){
        if(!productRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    @GetMapping("/{id}/history")
    @JsonView(Views.Output.class)
    public ResponseEntity<List<ProductHistory>> findHistoriesByProductId(@PathVariable Integer id){
        if(!productRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        List<ProductHistory> Histories = productHistoryRepository.findHistoriesByProductId(id);

        if(Histories.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product History not found");
        }

        return ResponseEntity.ok(Histories);

    }

}
