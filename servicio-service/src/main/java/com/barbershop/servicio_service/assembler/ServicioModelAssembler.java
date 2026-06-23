package com.barbershop.servicio_service.assembler;

import com.barbershop.servicio_service.controller.ServicioController;
import com.barbershop.servicio_service.dto.ServicioDTO;
import com.barbershop.servicio_service.model.Servicio;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ServicioModelAssembler
        implements RepresentationModelAssembler<Servicio, EntityModel<ServicioDTO>> {

    @Override
    public EntityModel<ServicioDTO> toModel(Servicio servicio) {

        ServicioDTO dto = ServicioDTO.fromModel(servicio);

        return EntityModel.of(dto,

                linkTo(methodOn(ServicioController.class)
                        .getById(servicio.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ServicioController.class)
                        .getAll())
                        .withRel("servicios"));
    }
}