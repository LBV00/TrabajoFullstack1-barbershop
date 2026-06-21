package com.barbershop.servicio_service.dto;

import com.barbershop.servicio_service.model.Servicio;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracion;

    public static ServicioDTO fromModel(Servicio servicio) {
        return new ServicioDTO(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio(),
                servicio.getDuracion()
        );
    }

    public Servicio toModel() {
        return new Servicio(
                id,
                nombre,
                descripcion,
                precio,
                duracion
        );
    }
}