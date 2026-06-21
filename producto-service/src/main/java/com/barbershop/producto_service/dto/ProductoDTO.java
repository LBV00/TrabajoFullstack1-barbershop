package com.barbershop.producto_service.dto;

import com.barbershop.producto_service.model.Producto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de un producto")
public class ProductoDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del producto", example = "Cera para cabello")
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @Schema(description = "Precio del producto", example = "8990")
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    @Schema(description = "Stock disponible", example = "25")
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    public static ProductoDTO fromModel(Producto p) {
        return ProductoDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .build();
    }

    public Producto toModel() {
        return Producto.builder()
                .id(this.id)
                .nombre(this.nombre)
                .precio(this.precio)
                .stock(this.stock)
                .build();
    }
}