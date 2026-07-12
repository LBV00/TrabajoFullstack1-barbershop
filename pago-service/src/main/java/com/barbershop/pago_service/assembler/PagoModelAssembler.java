package com.barbershop.pago_service.assembler;

import com.barbershop.pago_service.controller.PagoControllerV2;
import com.barbershop.pago_service.dto.PagoDTO;
import com.barbershop.pago_service.model.Pago;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoModelAssembler
        implements RepresentationModelAssembler<Pago, EntityModel<PagoDTO>> {

    @Override
    public EntityModel<PagoDTO> toModel(Pago pago) {

        PagoDTO dto = PagoDTO.fromModel(pago);

        return EntityModel.of(dto,

                linkTo(methodOn(PagoControllerV2.class)
                        .buscarPago(pago.getId()))
                        .withSelfRel(),

                linkTo(methodOn(PagoControllerV2.class)
                        .listarPagos())
                        .withRel("pagos"));
    }
}