package com.barbershop.producto_service.assembler;

import com.barbershop.producto_service.controller.ProductoControllerV2;
import com.barbershop.producto_service.dto.ProductoDTO;
import com.barbershop.producto_service.model.Producto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler
        implements RepresentationModelAssembler<Producto, EntityModel<ProductoDTO>> {

    @Override
    public EntityModel<ProductoDTO> toModel(Producto producto) {

        ProductoDTO dto = ProductoDTO.fromModel(producto);

        return EntityModel.of(dto,

                linkTo(methodOn(ProductoControllerV2.class)
                        .obtenerPorId(producto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ProductoControllerV2.class)
                        .getAll())
                        .withRel("productos"));
    }
}