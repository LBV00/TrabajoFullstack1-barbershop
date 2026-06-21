package com.barbershop.sucursal_service.dto;

import com.barbershop.sucursal_service.model.Sucursal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de una sucursal")
public class SucursalDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre de la sucursal", example = "Sucursal La Reina")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Dirección de la sucursal", example = "Av. Larraín 1234")
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    public static SucursalDTO fromModel(Sucursal sucursal) {
        return SucursalDTO.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .direccion(sucursal.getDireccion())
                .telefono(sucursal.getTelefono())
                .build();
    }

    public Sucursal toModel() {
        return Sucursal.builder()
                .id(this.id)
                .nombre(this.nombre)
                .direccion(this.direccion)
                .telefono(this.telefono)
                .build();
    }
}