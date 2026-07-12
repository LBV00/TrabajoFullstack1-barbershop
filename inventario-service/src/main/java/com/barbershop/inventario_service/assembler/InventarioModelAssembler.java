package com.barbershop.inventario_service.assembler;

import com.barbershop.inventario_service.controller.InventarioControllerV2;
import com.barbershop.inventario_service.dto.InventarioDTO;
import com.barbershop.inventario_service.model.Inventario;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InventarioModelAssembler
        implements RepresentationModelAssembler<Inventario, EntityModel<InventarioDTO>> {

    @Override
    public EntityModel<InventarioDTO> toModel(Inventario inventario) {

        InventarioDTO dto = InventarioDTO.fromModel(inventario);

        return EntityModel.of(dto,

                linkTo(methodOn(InventarioControllerV2.class)
                        .getById(inventario.getId()))
                        .withSelfRel(),

                linkTo(methodOn(InventarioControllerV2.class)
                        .getAll())
                        .withRel("inventarios"));
    }
}