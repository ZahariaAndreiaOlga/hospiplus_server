package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.Category;
import com.hospi.hospiplus.model.Product;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.repository.CategoryRepository;
import com.hospi.hospiplus.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
@Validated
@PreAuthorize("hasRole('ADMIN') or hasRole('ECO-DEP')")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<Category> findCategories(){
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public Category findCategory(@PathVariable Integer id){
        return categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    @PostMapping
    @JsonView(Views.Output.class)
    public ResponseEntity<Category> createCategory(@Valid @RequestBody @JsonView(Views.Input.class) Category category){
        LocalDateTime dtn = LocalDateTime.now();
        category.setCreatedAt(dtn);
        category.setUpdatedAt(dtn);
        categoryRepository.save(category);

        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Output.class)
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @Valid @RequestBody @JsonView(Views.Input.class) Category category){

        Category updateDbCategory = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        updateDbCategory.setCatgName(category.getCatgName());
        updateDbCategory.setUpdatedAt(LocalDateTime.now());

        categoryRepository.save(updateDbCategory);
        return ResponseEntity.ok(updateDbCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id){
        if(!categoryRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @GetMapping("/{id}/products")
    @JsonView(Views.Output.class)
    public ResponseEntity<List<Product>> findProductsByCategory(@PathVariable Integer id){

        if(!categoryRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        List<Product> products = productRepository.findProductsByCategoryId(id);

        if (products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return ResponseEntity.ok(products);
    }

}
