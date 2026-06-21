package com.barbershop.auth_service.controller;

import com.barbershop.auth_service.dto.LoginRequest;
import com.barbershop.auth_service.dto.LoginResponse;
import com.barbershop.auth_service.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones de autenticación JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    })
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request));
    }

    @GetMapping("/validar")
    @Operation(summary = "Validar token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    public ResponseEntity<Map<String, Object>> validar(
            @Parameter(description = "Token JWT", required = true)
            @RequestParam String token) {

        boolean valido =
                authService.validarToken(token);

        if (!valido) {
            return ResponseEntity.status(401)
                    .body(Map.of("valido", false));
        }

        return ResponseEntity.ok(Map.of(
                "valido", true,
                "username",
                authService.getUsernameDesdeToken(token)
        ));
    }
}