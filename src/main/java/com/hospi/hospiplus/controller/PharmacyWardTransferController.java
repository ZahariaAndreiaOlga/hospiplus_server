package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.*;
import com.hospi.hospiplus.repository.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/pharmacy-ward-transfer")
@Validated
@PreAuthorize("hasRole('ADMIN') or hasRole('ECO-DEP')")
@SecurityRequirement(name = "bearerAuth")
public class PharmacyWardTransferController {

    private final PharmacyWardTransferRepository transferRepository;
    private final WardRepository wardRepository;
    private final ProductRepository productRepository;
    private final ProductWardRepository productWardRepository;
    private final ProductWardHistoryRepository productWardHistoryRepository;
    private final ProductHistoryRepository productHistoryRepository;

    public PharmacyWardTransferController(
            PharmacyWardTransferRepository transferRepository,
            WardRepository wardRepository,
            ProductRepository productRepository,
            ProductWardRepository productWardRepository,
            ProductWardHistoryRepository productWardHistoryRepository,
            ProductHistoryRepository productHistoryRepository
    ) {
        this.transferRepository = transferRepository;
        this.wardRepository = wardRepository;
        this.productRepository = productRepository;
        this.productWardRepository = productWardRepository;
        this.productWardHistoryRepository = productWardHistoryRepository;
        this.productHistoryRepository = productHistoryRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<PharmacyWardTransfer> findAllTransfers() {
        return transferRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public PharmacyWardTransfer findTransfer(@PathVariable Integer id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found"));
    }

    @PostMapping
    @Transactional
    @JsonView(Views.Output.class)
    public ResponseEntity<PharmacyWardTransfer> createTransfer(
            @Valid @RequestBody @JsonView(Views.Input.class) PharmacyWardTransfer transfer
    ) {
        if (!wardRepository.existsById(transfer.getWardId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ward not found");
        }
        var product = productRepository.findById(transfer.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));

        // Ensure ProductWard exists (so we don't SELECT ProductWard inside triggers)
        var pw = productWardRepository.findByWardIdAndCode(transfer.getWardId(), product.getCode())
                .orElseGet(() -> {
                    ProductWard x = new ProductWard();
                    x.setWardId(transfer.getWardId());
                    x.setCode(product.getCode());
                    x.setQuantity(0);
                    x.setCriticalQuantity(0);
                    return productWardRepository.save(x);
                });

        // Persist the transfer record
        transferRepository.save(transfer);

        // Append history (append-only). Triggers on histories will update stock.
        var now = LocalDateTime.now();
        if ("To Ward".equals(transfer.getTypeOfTransfer())) {
            // Pharmacy -> Ward
            // 1) Subtract from pharmacy via ProductHistory
            ProductHistory ph = new ProductHistory();
            ph.setProductId(transfer.getProductId());
            ph.setQuantity(-transfer.getQuantity());
            ph.setCreatedAt(now);
            productHistoryRepository.save(ph);

            // 2) Add to ward via ProductWardHistory
            ProductWardHistory pwh = new ProductWardHistory();
            pwh.setProductWardId(pw.getId());
            pwh.setQuantity(transfer.getQuantity());
            pwh.setCreatedAt(now);
            productWardHistoryRepository.save(pwh);

        } else if ("To Pharmacy".equals(transfer.getTypeOfTransfer())) {
            // Ward -> Pharmacy
            // 1) Subtract from ward
            ProductWardHistory pwh = new ProductWardHistory();
            pwh.setProductWardId(pw.getId());
            pwh.setQuantity(-transfer.getQuantity());
            pwh.setCreatedAt(now);
            productWardHistoryRepository.save(pwh);

            // 2) Add back to pharmacy
            ProductHistory ph = new ProductHistory();
            ph.setProductId(transfer.getProductId());
            ph.setQuantity(transfer.getQuantity());
            ph.setCreatedAt(now);
            productHistoryRepository.save(ph);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown typeOfTransfer");
        }

        return ResponseEntity.ok(transfer);
    }

    @PutMapping("/{id}")
    @Transactional
    @JsonView(Views.Output.class)
    public ResponseEntity<PharmacyWardTransfer> updateTransfer(
            @PathVariable Integer id,
            @Valid @RequestBody @JsonView(Views.Input.class) PharmacyWardTransfer newT
    ) {
        PharmacyWardTransfer oldT = transferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found"));

        if (!wardRepository.existsById(newT.getWardId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ward not found");
        }
        var product = productRepository.findById(newT.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));

        var pw = productWardRepository.findByWardIdAndCode(newT.getWardId(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProductWard not found"));

        var now = LocalDateTime.now();

        // 1) Reverse the old transfer with compensating entries (append-only)
        if ("To Ward".equals(oldT.getTypeOfTransfer())) {
            // Old was Pharmacy -> Ward, so reverse: +pharmacy, -ward
            ProductHistory revPh = new ProductHistory();
            revPh.setProductId(oldT.getProductId());
            revPh.setQuantity(oldT.getQuantity()); // add back to pharmacy
            revPh.setCreatedAt(now);
            productHistoryRepository.save(revPh);

            ProductWardHistory revPwh = new ProductWardHistory();
            revPwh.setProductWardId(pw.getId());
            revPwh.setQuantity(-oldT.getQuantity()); // remove from ward
            revPwh.setCreatedAt(now);
            productWardHistoryRepository.save(revPwh);

        } else if ("To Pharmacy".equals(oldT.getTypeOfTransfer())) {
            // Old was Ward -> Pharmacy, so reverse: +ward, -pharmacy
            ProductWardHistory revPwh = new ProductWardHistory();
            revPwh.setProductWardId(pw.getId());
            revPwh.setQuantity(oldT.getQuantity()); // add back to ward
            revPwh.setCreatedAt(now);
            productWardHistoryRepository.save(revPwh);

            ProductHistory revPh = new ProductHistory();
            revPh.setProductId(oldT.getProductId());
            revPh.setQuantity(-oldT.getQuantity()); // remove from pharmacy
            revPh.setCreatedAt(now);
            productHistoryRepository.save(revPh);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown old typeOfTransfer");
        }

        // 2) Apply the new transfer
        if ("To Ward".equals(newT.getTypeOfTransfer())) {
            ProductHistory ph = new ProductHistory();
            ph.setProductId(newT.getProductId());
            ph.setQuantity(-newT.getQuantity());
            ph.setCreatedAt(now);
            productHistoryRepository.save(ph);

            ProductWardHistory pwh = new ProductWardHistory();
            pwh.setProductWardId(pw.getId());
            pwh.setQuantity(newT.getQuantity());
            pwh.setCreatedAt(now);
            productWardHistoryRepository.save(pwh);

        } else if ("To Pharmacy".equals(newT.getTypeOfTransfer())) {
            ProductWardHistory pwh = new ProductWardHistory();
            pwh.setProductWardId(pw.getId());
            pwh.setQuantity(-newT.getQuantity());
            pwh.setCreatedAt(now);
            productWardHistoryRepository.save(pwh);

            ProductHistory ph = new ProductHistory();
            ph.setProductId(newT.getProductId());
            ph.setQuantity(newT.getQuantity());
            ph.setCreatedAt(now);
            productHistoryRepository.save(ph);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown new typeOfTransfer");
        }

        // 3) Update transfer record
        oldT.setWardId(newT.getWardId());
        oldT.setProductId(newT.getProductId());
        oldT.setQuantity(newT.getQuantity());
        oldT.setTypeOfTransfer(newT.getTypeOfTransfer());
        transferRepository.save(oldT);

        return ResponseEntity.ok(oldT);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteTransfer(@PathVariable Integer id) {
        PharmacyWardTransfer t = transferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found"));

        var product = productRepository.findById(t.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));
        var pw = productWardRepository.findByWardIdAndCode(t.getWardId(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProductWard not found"));

        var now = LocalDateTime.now();

        // Insert compensating entries (append-only) to undo the transfer
        if ("To Ward".equals(t.getTypeOfTransfer())) {
            // Reverse: +pharmacy, -ward
            ProductHistory ph = new ProductHistory();
            ph.setProductId(t.getProductId());
            ph.setQuantity(t.getQuantity());
            ph.setCreatedAt(now);
            productHistoryRepository.save(ph);

            ProductWardHistory pwh = new ProductWardHistory();
            pwh.setProductWardId(pw.getId());
            pwh.setQuantity(-t.getQuantity());
            pwh.setCreatedAt(now);
            productWardHistoryRepository.save(pwh);

        } else if ("To Pharmacy".equals(t.getTypeOfTransfer())) {
            // Reverse: +ward, -pharmacy
            ProductWardHistory pwh = new ProductWardHistory();
            pwh.setProductWardId(pw.getId());
            pwh.setQuantity(t.getQuantity());
            pwh.setCreatedAt(now);
            productWardHistoryRepository.save(pwh);

            ProductHistory ph = new ProductHistory();
            ph.setProductId(t.getProductId());
            ph.setQuantity(-t.getQuantity());
            ph.setCreatedAt(now);
            productHistoryRepository.save(ph);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown typeOfTransfer");
        }

        // Finally delete the transfer record
        transferRepository.delete(t);
    }
}
