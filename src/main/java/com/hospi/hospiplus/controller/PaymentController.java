package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.Payment;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/payment")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<Payment> findPayments(){
        return paymentRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public Payment findPayment(@PathVariable Integer id){
        return paymentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @PostMapping
    @JsonView(Views.Output.class)
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody @JsonView(Views.Input.class) Payment payment){
        paymentRepository.save(payment);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Output.class)
    public ResponseEntity<Payment>  updatePayment(@PathVariable Integer id, @Valid @RequestBody @JsonView(Views.Input.class) Payment payment){
        if(!paymentRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        payment.setId(id);
        paymentRepository.save(payment);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Integer id){
        if(!paymentRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        paymentRepository.deleteById(id);
    }

}
