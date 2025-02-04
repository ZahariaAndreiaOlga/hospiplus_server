package com.hospi.hospiplus.controller;

import com.hospi.hospiplus.model.DTO.OrderDetailsDTO;
import com.hospi.hospiplus.model.DTO.BasketOrdered;
import com.hospi.hospiplus.model.Order;
import com.hospi.hospiplus.model.OrderedProduct;
import com.hospi.hospiplus.model.Product;
import com.hospi.hospiplus.repository.OrderRepository;
import com.hospi.hospiplus.repository.OrderedProductRepository;
import com.hospi.hospiplus.repository.ProductRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Validated
@RequestMapping("/api/v1/order")
@PreAuthorize("hasRole('ADMIN') or hasRole('ECO-DEP')")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderedProductRepository orderedProductRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository, OrderedProductRepository orderedProductRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderedProductRepository = orderedProductRepository;
    }

    @GetMapping
    public Iterable<Order> findOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order findOrder(@PathVariable Integer id){
        return orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody List<BasketOrdered> productsOrdered){

        Set<OrderedProduct> orderedProductList = new HashSet<>();
        Float totalPrice = 0.00f;
        Integer quantity = 0;

        for(BasketOrdered basketOrdered : productsOrdered){

            Product product = productRepository.findById(basketOrdered.getProductId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            OrderedProduct orderedProduct = new OrderedProduct();

            orderedProduct.setProductId(basketOrdered.getProductId());
            orderedProduct.setQuantity(basketOrdered.getQuantity());
            orderedProduct.setPricePerUnit(product.getPricePerUnit());
            orderedProduct.setTotalPrice(basketOrdered.getQuantity() * product.getPricePerUnit());

            orderedProductList.add(orderedProduct);

            quantity += orderedProduct.getQuantity();
            totalPrice += orderedProduct.getTotalPrice();

        }

        Random rand = new Random();
        Order order = new Order();
        order.setOrderedAt(LocalDateTime.now());
        order.setArrival(LocalDate.now());
        order.setOrderNum(rand.nextInt(1_000_000_000));
        order.setPricePerProduct(0.00f);
        order.setTotalPrice(totalPrice);
        order.setQuantity(quantity);
        order.setUserId(2); // TEST - Change for the Token User ID
        order.setProducts(orderedProductList);

        orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order order){

        Order updateDbOrder = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        updateDbOrder.setOrderNum(order.getOrderNum());
        updateDbOrder.setArrival(order.getArrival());
        updateDbOrder.setQuantity(order.getQuantity());
        updateDbOrder.setPricePerProduct(order.getPricePerProduct());
        updateDbOrder.setTotalPrice(order.getTotalPrice());

        orderRepository.save(updateDbOrder);
        return ResponseEntity.ok(updateDbOrder);

    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Integer id){
        if(!orderRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteById(id);
    }

    @GetMapping("/{id}/details")
    public List<OrderDetailsDTO> findOrderDetails(@PathVariable Integer id){
        if(!orderRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return orderRepository.findOrderDetailsByOrderId(id);
    }

}
