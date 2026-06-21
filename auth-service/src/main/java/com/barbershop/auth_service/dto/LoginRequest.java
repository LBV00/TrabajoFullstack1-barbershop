package com.barbershop.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Solicitud de autenticación")
public class LoginRequest {

    @Schema(description = "Nombre de usuario", example = "admin")
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "admin123")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}