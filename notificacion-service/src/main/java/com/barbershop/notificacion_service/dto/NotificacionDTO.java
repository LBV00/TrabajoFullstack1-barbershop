package com.barbershop.notificacion_service.dto;

import com.barbershop.notificacion_service.model.Notificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de una notificación")
public class NotificacionDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Destinatario de la notificación", example = "usuario@gmail.com")
    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatario;

    @Schema(description = "Mensaje de la notificación", example = "Su reserva fue confirmada")
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @Schema(description = "Estado de la notificación", example = "ENVIADA")
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    public static NotificacionDTO fromModel(Notificacion n) {
        return NotificacionDTO.builder()
                .id(n.getId())
                .destinatario(n.getDestinatario())
                .mensaje(n.getMensaje())
                .estado(n.getEstado())
                .build();
    }

    public Notificacion toModel() {
        return Notificacion.builder()
                .id(this.id)
                .destinatario(this.destinatario)
                .mensaje(this.mensaje)
                .estado(this.estado)
                .build();
    }
}