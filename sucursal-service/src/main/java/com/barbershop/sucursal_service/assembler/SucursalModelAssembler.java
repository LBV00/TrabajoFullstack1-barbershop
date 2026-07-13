package com.barbershop.sucursal_service.assembler;

import com.barbershop.sucursal_service.controller.SucursalControllerV2;
import com.barbershop.sucursal_service.dto.SucursalDTO;
import com.barbershop.sucursal_service.model.Sucursal;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SucursalModelAssembler
        implements RepresentationModelAssembler<Sucursal, EntityModel<SucursalDTO>> {

    @Override
    public EntityModel<SucursalDTO> toModel(Sucursal sucursal) {

        SucursalDTO dto = SucursalDTO.fromModel(sucursal);

        return EntityModel.of(dto,

                linkTo(methodOn(SucursalControllerV2.class)
                        .getById(sucursal.getId()))
                        .withSelfRel(),

                linkTo(methodOn(SucursalControllerV2.class)
                        .getAll())
                        .withRel("sucursales"));
    }
}