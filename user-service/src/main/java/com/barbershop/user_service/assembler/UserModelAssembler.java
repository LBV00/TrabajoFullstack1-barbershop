package com.barbershop.user_service.assembler;

import com.barbershop.user_service.controller.UserController;
import com.barbershop.user_service.model.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler
        implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {

        return EntityModel.of(user,

                linkTo(methodOn(UserController.class)
                        .getById(user.getId()))
                        .withSelfRel(),

                linkTo(methodOn(UserController.class)
                        .getAll())
                        .withRel("usuarios"));
    }
}