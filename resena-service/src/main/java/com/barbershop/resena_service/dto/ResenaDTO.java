package com.barbershop.resena_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de una reseña")
public class ResenaDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario que realiza la reseña", example = "1")
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @Schema(description = "ID de la reserva asociada", example = "1")
    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    @Schema(description = "Calificación de la reseña", example = "5")
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @Schema(description = "Comentario de la reseña", example = "Excelente servicio")
    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;

    public static ResenaDTO fromModel(com.barbershop.resena_service.model.Resena model) {
        return ResenaDTO.builder()
                .id(model.getId())
                .idUsuario(model.getIdUsuario())
                .idReserva(model.getIdReserva())
                .calificacion(model.getCalificacion())
                .comentario(model.getComentario())
                .build();
    }
}