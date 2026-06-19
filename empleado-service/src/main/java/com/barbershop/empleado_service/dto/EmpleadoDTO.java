package com.barbershop.empleado_service.dto;

import com.barbershop.empleado_service.model.Empleado;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {

    private Long id;

    private String nombre;

    private String especialidad;

    private String telefono;

    private Boolean disponible;

    public static EmpleadoDTO fromModel(Empleado empleado){
        return new EmpleadoDTO(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getEspecialidad(),
                empleado.getTelefono(),
                empleado.getDisponible()
        );
    }

    public Empleado toModel(){
        return new Empleado(
                id,
                nombre,
                especialidad,
                telefono,
                disponible
        );
    }
}