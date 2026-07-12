package com.barbershop.notificacion_service.assembler;

import com.barbershop.notificacion_service.controller.NotificacionControllerV2;
import com.barbershop.notificacion_service.dto.NotificacionDTO;
import com.barbershop.notificacion_service.model.Notificacion;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class NotificacionModelAssembler
        implements RepresentationModelAssembler<Notificacion, EntityModel<NotificacionDTO>> {

    @Override
    public EntityModel<NotificacionDTO> toModel(Notificacion notificacion) {

        NotificacionDTO dto = NotificacionDTO.fromModel(notificacion);

        return EntityModel.of(dto,

                linkTo(methodOn(NotificacionControllerV2.class)
                        .getById(notificacion.getId()))
                        .withSelfRel(),

                linkTo(methodOn(NotificacionControllerV2.class)
                        .getAll())
                        .withRel("notificaciones"));
    }
}