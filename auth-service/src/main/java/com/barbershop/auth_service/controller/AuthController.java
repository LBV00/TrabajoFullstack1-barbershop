package com.barbershop.auth_service.controller;

import com.barbershop.auth_service.dto.LoginRequest;
import com.barbershop.auth_service.dto.LoginResponse;
import com.barbershop.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/validar")
    public ResponseEntity<Map<String, Object>> validar(@RequestParam String token) {
        boolean valido = authService.validarToken(token);
        if (!valido) {
            return ResponseEntity.status(401).body(Map.of("valido", false));
        }
        return ResponseEntity.ok(Map.of(
                "valido", true,
                "username", authService.getUsernameDesdeToken(token)
        ));
    }
}