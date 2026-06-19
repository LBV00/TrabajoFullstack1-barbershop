package com.barbershop.sucursal_service.dto;

import com.barbershop.sucursal_service.model.Sucursal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    
    public static SucursalDTO fromModel(Sucursal sucursal) {
        return new SucursalDTO(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion(),
                sucursal.getTelefono()
        );
    }

    public Sucursal toModel() {
        return new Sucursal(
                id,
                nombre,
                direccion,
                telefono
        );
    }
}