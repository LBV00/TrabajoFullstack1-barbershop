package com.barbershop.sucursal_service.assembler;

import com.barbershop.sucursal_service.controller.SucursalController;
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

                linkTo(methodOn(SucursalController.class)
                        .getById(sucursal.getId()))
                        .withSelfRel(),

                linkTo(methodOn(SucursalController.class)
                        .getAll())
                        .withRel("sucursales"));
    }
}