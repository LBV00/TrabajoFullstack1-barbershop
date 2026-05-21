package com.barbershop.producto_service.dto;

import com.barbershop.producto_service.model.Producto;
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
public class ProductoDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;
    
    // CORRECCIÓN: Cambiamos descripcion por stock y añadimos validación
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo") 
    private Integer stock;

    public static ProductoDTO fromModel(Producto p) {
        return ProductoDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .stock(p.getStock()) // Ahora sí llama al stock
                .build();
    }

    public Producto toModel() {
        return Producto.builder()
                .id(this.id)
                .nombre(this.nombre)
                .precio(this.precio)
                .stock(this.stock) // Ahora sí llama al stock
                .build();
    }
}