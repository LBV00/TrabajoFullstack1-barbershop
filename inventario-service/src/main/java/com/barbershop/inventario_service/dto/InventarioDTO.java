package com.barbershop.inventario_service.dto;

import com.barbershop.inventario_service.model.Inventario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de inventario")
public class InventarioDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del producto asociado", example = "1")
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Schema(description = "Stock disponible", example = "25")
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    @Schema(description = "Ubicación física del producto", example = "Bodega Central")
    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;

    public static InventarioDTO fromModel(Inventario inventario) {
        return InventarioDTO.builder()
                .id(inventario.getId())
                .productoId(inventario.getProductoId())
                .stock(inventario.getStock())
                .ubicacion(inventario.getUbicacion())
                .build();
    }

    public Inventario toModel() {
        return Inventario.builder()
                .id(this.id)
                .productoId(this.productoId)
                .stock(this.stock)
                .ubicacion(this.ubicacion)
                .build();
    }
}