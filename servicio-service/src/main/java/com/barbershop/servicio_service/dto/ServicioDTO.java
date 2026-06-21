package com.barbershop.servicio_service.dto;

import com.barbershop.servicio_service.model.Servicio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de un servicio")
public class ServicioDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del servicio", example = "Corte Fade")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Descripción del servicio", example = "Corte degradado moderno")
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Schema(description = "Precio del servicio", example = "12000")
    @NotNull(message = "El precio es obligatorio")
    private Double precio;

    @Schema(description = "Duración en minutos", example = "45")
    @NotNull(message = "La duración es obligatoria")
    private Integer duracion;

    public static ServicioDTO fromModel(Servicio servicio) {
        return ServicioDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .precio(servicio.getPrecio())
                .duracion(servicio.getDuracion())
                .build();
    }

    public Servicio toModel() {
        return Servicio.builder()
                .id(this.id)
                .nombre(this.nombre)
                .descripcion(this.descripcion)
                .precio(this.precio)
                .duracion(this.duracion)
                .build();
    }
}