package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.DTO.UserDTO;
import com.hospi.hospiplus.model.Order;
import com.hospi.hospiplus.model.User;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.repository.OrderRepository;
import com.hospi.hospiplus.repository.UserRepository;
import com.hospi.hospiplus.utils.CustomAuthenticationToken;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1/user")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, OrderRepository orderRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public Iterable<UserDTO> findUsers(){
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO findUser(@PathVariable Integer id){
       return userRepository.findById(id)
               .map(user -> new UserDTO(user))
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping
    @JsonView(Views.Output.class)
    public User createUser(@Valid @RequestBody @JsonView(Views.Input.class) User user){

        if(userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

        LocalDateTime dtn = LocalDateTime.now();
        user.setCreatedAt(dtn);
        user.setUpdatedAt(dtn);
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Output.class)
    public User updateUser(@PathVariable Integer id, @Valid  @RequestBody @JsonView(Views.Input.class) User user){
        User updateDbUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        updateDbUser.setRoleId(user.getRoleId());
        updateDbUser.setSurname(user.getSurname());
        updateDbUser.setUserName(user.getUserName());

        return userRepository.save(updateDbUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id){
        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    @GetMapping("/{id}/orders")
    @JsonView(Views.Output.class)
    public ResponseEntity<List<Order>> findOrdersByUserId(@PathVariable Integer id){
        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<Order> orders = orderRepository.findOrderByUserId(id);

        if(orders.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        return ResponseEntity.ok(orders);

    }

}
