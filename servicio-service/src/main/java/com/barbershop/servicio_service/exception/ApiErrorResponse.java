package com.barbershop.servicio_service.exception;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar de error")
public class ApiErrorResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
