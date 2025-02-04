package com.hospi.hospiplus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hospi.hospiplus.model.DTO.UserLoginDTO;
import com.hospi.hospiplus.model.Role;
import com.hospi.hospiplus.model.User;
import com.hospi.hospiplus.model.Views;
import com.hospi.hospiplus.repository.RoleRepository;
import com.hospi.hospiplus.repository.UserRepository;
import com.hospi.hospiplus.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String errorMessage) {
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage);
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody @JsonView(Views.Input.class) User user){
        try {
            if(userRepository.findUserByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            LocalDateTime dtn = LocalDateTime.now();
            user.setCreatedAt(dtn);
            user.setUpdatedAt(dtn);
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            user.setRoleId(3); // DEFAULT ROLE USER
            userRepository.save(user);

            Optional<Role> role = roleRepository.findById(3);
            if(role.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid role");
            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), role.get().getRoleName());

            String jsonResponse = String.format("{\"token\":\"%s\", \"role\":\"%s\"}", token, role.get().getRoleName());

            return ResponseEntity.ok(jsonResponse);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try{
            Optional<User> user = userRepository.findUserByEmail(userLoginDTO.getEmail());

            if(user.isPresent() && passwordEncoder.matches(userLoginDTO.getPassword(), user.get().getUserPassword())){

                Optional<Role> role = roleRepository.findById(user.get().getRoleId());
                if(role.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid role");

                String token = jwtUtil.generateToken(user.get().getId(), user.get().getEmail(),role.get().getRoleName());

                String jsonResponse = String.format("{\"token\":\"%s\", \"role\":\"%s\"}", token, role.get().getRoleName());

                return ResponseEntity.ok(jsonResponse);

            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again.");
        }
    }

}
