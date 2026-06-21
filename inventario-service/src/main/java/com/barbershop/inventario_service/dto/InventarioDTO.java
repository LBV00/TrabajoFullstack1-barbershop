package com.barbershop.inventario_service.dto;

import com.barbershop.inventario_service.model.Inventario;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {

    private Long id;
    private Long productoId;
    private Integer stock;
    private String ubicacion;

    public static InventarioDTO fromModel(Inventario inventario) {
        return new InventarioDTO(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getStock(),
                inventario.getUbicacion()
        );
    }

    public Inventario toModel() {
        return new Inventario(
                id,
                productoId,
                stock,
                ubicacion
        );
    }
}