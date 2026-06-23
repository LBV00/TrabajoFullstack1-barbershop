package com.barbershop.user_service.controller;

import com.barbershop.user_service.assembler.UserModelAssembler;
import com.barbershop.user_service.model.User;
import com.barbershop.user_service.service.UserService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserModelAssembler assembler;

    @Test
    void debeListarUsuarios() throws Exception {

        User user1 = User.builder()
                .id(1L)
                .nombre("Martin")
                .build();

        User user2 = User.builder()
                .id(2L)
                .nombre("Juan")
                .build();

        when(userService.findAll())
                .thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarUsuarioPorId() throws Exception {

        User user = User.builder()
                .id(1L)
                .nombre("Martin")
                .build();

        when(userService.findById(1L))
                .thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeRetornar404CuandoUsuarioNoExiste() throws Exception {

        when(userService.findById(99L))
                .thenReturn(null);

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }
}