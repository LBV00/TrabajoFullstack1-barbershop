package com.barbershop.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación")
public class LoginResponse {

    @Schema(description = "JWT generado")
    private String token;

    @Schema(description = "Nombre de usuario")
    private String username;

    @Schema(description = "Rol del usuario")
    private String rol;

    @Schema(description = "Mensaje de respuesta")
    private String mensaje;
}