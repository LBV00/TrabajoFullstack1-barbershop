package com.barbershop.resena_service.assembler;

import com.barbershop.resena_service.controller.ResenaController;
import com.barbershop.resena_service.dto.ResenaDTO;
import com.barbershop.resena_service.model.Resena;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ResenaModelAssembler
        implements RepresentationModelAssembler<Resena, EntityModel<ResenaDTO>> {

    @Override
    public EntityModel<ResenaDTO> toModel(Resena resena) {

        ResenaDTO dto = ResenaDTO.builder()
                .id(resena.getId())
                .idUsuario(resena.getIdUsuario())
                .idReserva(resena.getIdReserva())
                .calificacion(resena.getCalificacion())
                .comentario(resena.getComentario())
                .build();

        return EntityModel.of(dto,

                linkTo(methodOn(ResenaController.class)
                        .getById(resena.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ResenaController.class)
                        .getAll())
                        .withRel("resenas"));
    }
}