package com.barbershop.empleado_service.assembler;

import com.barbershop.empleado_service.controller.EmpleadoControllerV2;
import com.barbershop.empleado_service.dto.EmpleadoDTO;
import com.barbershop.empleado_service.model.Empleado;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EmpleadoModelAssembler
        implements RepresentationModelAssembler<Empleado, EntityModel<EmpleadoDTO>> {

    @Override
    public EntityModel<EmpleadoDTO> toModel(Empleado empleado) {

        EmpleadoDTO dto = EmpleadoDTO.fromModel(empleado);

        return EntityModel.of(dto,

                linkTo(methodOn(EmpleadoControllerV2.class)
                        .getById(empleado.getId()))
                        .withSelfRel(),

                linkTo(methodOn(EmpleadoControllerV2.class)
                        .getAll())
                        .withRel("empleados"));
    }
}