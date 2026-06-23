package com.barbershop.sucursal_service.controller;

import com.barbershop.sucursal_service.assembler.SucursalModelAssembler;
import com.barbershop.sucursal_service.dto.SucursalDTO;
import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.service.SucursalService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalService sucursalService;

    @MockBean
    private SucursalModelAssembler assembler;

    @Test
    void debeListarSucursales() throws Exception {

        Sucursal s1 = Sucursal.builder()
                .id(1L)
                .nombre("La Reina")
                .direccion("Direccion 1")
                .telefono("123")
                .build();

        Sucursal s2 = Sucursal.builder()
                .id(2L)
                .nombre("Providencia")
                .direccion("Direccion 2")
                .telefono("456")
                .build();

        when(sucursalService.findAll())
                .thenReturn(Arrays.asList(s1, s2));

        when(assembler.toModel(any(Sucursal.class)))
                .thenReturn(EntityModel.of(new SucursalDTO()));

        mockMvc.perform(get("/sucursales"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarSucursalPorId() throws Exception {

        Sucursal sucursal = Sucursal.builder()
                .id(1L)
                .nombre("La Reina")
                .direccion("Direccion")
                .telefono("123")
                .build();

        when(sucursalService.findById(1L))
                .thenReturn(sucursal);

        when(assembler.toModel(any(Sucursal.class)))
                .thenReturn(EntityModel.of(new SucursalDTO()));

        mockMvc.perform(get("/sucursales/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeRetornar404CuandoSucursalNoExiste() throws Exception {

        when(sucursalService.findById(99L))
                .thenReturn(null);

        mockMvc.perform(get("/sucursales/99"))
                .andExpect(status().isNotFound());
    }
}