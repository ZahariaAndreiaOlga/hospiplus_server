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
@RequestMapping("/api/v1/ward-transfer")
@Validated
@PreAuthorize("hasRole('ADMIN') or hasRole('WARD-MANAGER')")
@SecurityRequirement(name = "bearerAuth")
public class WardTransferController {

    private final WardTransferRepository wardTransferRepository;
    private final WardRepository wardRepository;
    private final ProductRepository productRepository;
    private final ProductWardRepository productWardRepository;
    private final ProductWardHistoryRepository productWardHistoryRepository;

    public WardTransferController(
            WardTransferRepository wardTransferRepository,
            WardRepository wardRepository,
            ProductRepository productRepository,
            ProductWardRepository productWardRepository,
            ProductWardHistoryRepository productWardHistoryRepository
    ) {
        this.wardTransferRepository = wardTransferRepository;
        this.wardRepository = wardRepository;
        this.productRepository = productRepository;
        this.productWardRepository = productWardRepository;
        this.productWardHistoryRepository = productWardHistoryRepository;
    }

    @GetMapping
    @JsonView(Views.Output.class)
    public Iterable<WardTransfer> findTransfers() {
        return wardTransferRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Output.class)
    public WardTransfer findTransfer(@PathVariable Integer id) {
        return wardTransferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WardTransfer not found"));
    }

    @PostMapping
    @Transactional
    @JsonView(Views.Output.class)
    public ResponseEntity<WardTransfer> createTransfer(
            @Valid @RequestBody @JsonView(Views.Input.class) WardTransfer transfer
    ) {
        if (!wardRepository.existsById(transfer.getGiver())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid giver ward");
        }
        if (!wardRepository.existsById(transfer.getReciever())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid receiver ward");
        }
        var product = productRepository.findById(transfer.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product"));

        var giverPw = productWardRepository.findByWardIdAndCode(transfer.getGiver(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giver ProductWard not found"));
        var receiverPw = productWardRepository.findByWardIdAndCode(transfer.getReciever(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver ProductWard not found"));

        // Persist the transfer
        wardTransferRepository.save(transfer);

        // Append history (append-only). Triggers on ProductWardHistory update ward stocks.
        var now = LocalDateTime.now();

        ProductWardHistory giverHist = new ProductWardHistory();
        giverHist.setProductWardId(giverPw.getId());
        giverHist.setQuantity(-transfer.getQuantity());
        giverHist.setCreatedAt(now);
        productWardHistoryRepository.save(giverHist);

        ProductWardHistory receiverHist = new ProductWardHistory();
        receiverHist.setProductWardId(receiverPw.getId());
        receiverHist.setQuantity(transfer.getQuantity());
        receiverHist.setCreatedAt(now);
        productWardHistoryRepository.save(receiverHist);

        return ResponseEntity.ok(transfer);
    }

    @PutMapping("/{id}")
    @Transactional
    @JsonView(Views.Output.class)
    public ResponseEntity<WardTransfer> updateTransfer(
            @PathVariable Integer id,
            @Valid @RequestBody @JsonView(Views.Input.class) WardTransfer newT
    ) {
        WardTransfer oldT = wardTransferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WardTransfer not found"));

        if (!wardRepository.existsById(newT.getGiver())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid giver ward");
        }
        if (!wardRepository.existsById(newT.getReciever())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid receiver ward");
        }
        var product = productRepository.findById(newT.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product"));

        var oldGiverPw = productWardRepository.findByWardIdAndCode(oldT.getGiver(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old giver ProductWard not found"));
        var oldReceiverPw = productWardRepository.findByWardIdAndCode(oldT.getReciever(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old receiver ProductWard not found"));

        var now = LocalDateTime.now();

        // 1) Reverse the old transfer (append-only)
        ProductWardHistory revGiver = new ProductWardHistory();
        revGiver.setProductWardId(oldGiverPw.getId());
        revGiver.setQuantity(oldT.getQuantity()); // give back to old giver
        revGiver.setCreatedAt(now);
        productWardHistoryRepository.save(revGiver);

        ProductWardHistory revReceiver = new ProductWardHistory();
        revReceiver.setProductWardId(oldReceiverPw.getId());
        revReceiver.setQuantity(-oldT.getQuantity()); // take back from old receiver
        revReceiver.setCreatedAt(now);
        productWardHistoryRepository.save(revReceiver);

        // 2) Apply the new transfer
        var newGiverPw = productWardRepository.findByWardIdAndCode(newT.getGiver(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "New giver ProductWard not found"));
        var newReceiverPw = productWardRepository.findByWardIdAndCode(newT.getReciever(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "New receiver ProductWard not found"));

        ProductWardHistory giverHist = new ProductWardHistory();
        giverHist.setProductWardId(newGiverPw.getId());
        giverHist.setQuantity(-newT.getQuantity());
        giverHist.setCreatedAt(now);
        productWardHistoryRepository.save(giverHist);

        ProductWardHistory receiverHist = new ProductWardHistory();
        receiverHist.setProductWardId(newReceiverPw.getId());
        receiverHist.setQuantity(newT.getQuantity());
        receiverHist.setCreatedAt(now);
        productWardHistoryRepository.save(receiverHist);

        // 3) Update the transfer record
        oldT.setGiver(newT.getGiver());
        oldT.setReciever(newT.getReciever());
        oldT.setProductId(newT.getProductId());
        oldT.setQuantity(newT.getQuantity());
        oldT.setTransferType(newT.getTransferType());
        wardTransferRepository.save(oldT);

        return ResponseEntity.ok(oldT);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteTransfer(@PathVariable Integer id) {
        WardTransfer t = wardTransferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WardTransfer not found"));

        var product = productRepository.findById(t.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product"));

        var giverPw = productWardRepository.findByWardIdAndCode(t.getGiver(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giver ProductWard not found"));
        var receiverPw = productWardRepository.findByWardIdAndCode(t.getReciever(), product.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver ProductWard not found"));

        var now = LocalDateTime.now();

        // Insert compensating entries to undo the transfer (append-only)
        ProductWardHistory revGiver = new ProductWardHistory();
        revGiver.setProductWardId(giverPw.getId());
        revGiver.setQuantity(t.getQuantity()); // return to giver
        revGiver.setCreatedAt(now);
        productWardHistoryRepository.save(revGiver);

        ProductWardHistory revReceiver = new ProductWardHistory();
        revReceiver.setProductWardId(receiverPw.getId());
        revReceiver.setQuantity(-t.getQuantity()); // remove from receiver
        revReceiver.setCreatedAt(now);
        productWardHistoryRepository.save(revReceiver);

        // Finally delete the transfer record
        wardTransferRepository.delete(t);
    }
}
