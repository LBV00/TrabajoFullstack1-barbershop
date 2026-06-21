package com.barbershop.empleado_service.dto;

import com.barbershop.empleado_service.model.Empleado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de un empleado")
public class EmpleadoDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del empleado", example = "Juan Pérez")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Especialidad del empleado", example = "Barbero")
    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @Schema(description = "Disponibilidad del empleado", example = "true")
    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;

    public static EmpleadoDTO fromModel(Empleado empleado) {
        return EmpleadoDTO.builder()
                .id(empleado.getId())
                .nombre(empleado.getNombre())
                .especialidad(empleado.getEspecialidad())
                .telefono(empleado.getTelefono())
                .disponible(empleado.getDisponible())
                .build();
    }

    public Empleado toModel() {
        return Empleado.builder()
                .id(this.id)
                .nombre(this.nombre)
                .especialidad(this.especialidad)
                .telefono(this.telefono)
                .disponible(this.disponible)
                .build();
    }
}