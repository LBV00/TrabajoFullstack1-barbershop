package com.barbershop.sucursal_service.dto;

import com.barbershop.sucursal_service.model.Sucursal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO extends RepresentationModel<SucursalDTO> {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;

    public static SucursalDTO fromModel(Sucursal sucursal) {

        SucursalDTO dto = new SucursalDTO();

        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccion(sucursal.getDireccion());
        dto.setTelefono(sucursal.getTelefono());

        return dto;
    }

    public Sucursal toModel() {

        Sucursal sucursal = new Sucursal();

        sucursal.setId(this.id);
        sucursal.setNombre(this.nombre);
        sucursal.setDireccion(this.direccion);
        sucursal.setTelefono(this.telefono);

        return sucursal;
    }
}