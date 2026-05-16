package com.barbershop.user_service.controller;

import com.barbershop.user_service.dto.UserDTO;
import com.barbershop.user_service.model.User;
import com.barbershop.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        List<UserDTO> dtos = users.stream().map(UserDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(UserDTO.fromModel(user));
        }
        return ResponseEntity.notFound().build(); // 404
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        User savedUser = userService.save(userDTO.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromModel(savedUser)); // 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        if (userService.findById(id) != null) {
            userDTO.setId(id); 
            User updatedUser = userService.save(userDTO.toModel());
            return ResponseEntity.ok(UserDTO.fromModel(updatedUser));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userService.findById(id) != null) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint remoto para validar existencia desde Reserva-Service
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.existsById(id));
    }
}
