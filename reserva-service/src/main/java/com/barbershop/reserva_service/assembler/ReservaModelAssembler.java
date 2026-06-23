package com.barbershop.reserva_service.assembler;

import com.barbershop.reserva_service.controller.ReservaController;
import com.barbershop.reserva_service.dto.ReservaDTO;
import com.barbershop.reserva_service.model.Reserva;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReservaModelAssembler
        implements RepresentationModelAssembler<Reserva, EntityModel<ReservaDTO>> {

    @Override
    public EntityModel<ReservaDTO> toModel(Reserva reserva) {

        ReservaDTO dto = ReservaDTO.builder()
                .id(reserva.getId())
                .idUsuario(reserva.getIdUsuario())
                .fechaReserva(reserva.getFechaReserva())
                .total(reserva.getTotal())
                .estado(reserva.getEstado())
                .build();

        return EntityModel.of(dto,

                linkTo(methodOn(ReservaController.class)
                        .getById(reserva.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ReservaController.class)
                        .getAll())
                        .withRel("reservas"));
    }
}