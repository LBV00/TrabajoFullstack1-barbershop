package com.barbershop.producto_service.dto;

import com.barbershop.producto_service.model.Producto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    
    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String nombre;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;
    
    private String descripcion;

    public static ProductoDTO fromModel(Producto p) {
        return ProductoDTO.builder().id(p.getId()).nombre(p.getNombre()).precio(p.getPrecio()).descripcion(p.getDescripcion()).build();
    }

    public Producto toModel() {
        return Producto.builder().id(this.id).nombre(this.nombre).precio(this.precio).descripcion(this.descripcion).build();
    }
}