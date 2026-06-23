package com.barbershop.inventario_service.controller;

import com.barbershop.inventario_service.assembler.InventarioModelAssembler;
import com.barbershop.inventario_service.dto.InventarioDTO;
import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.service.InventarioService;

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

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private InventarioModelAssembler assembler;

    @Test
    void debeListarInventarios() throws Exception {

        Inventario i1 = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .stock(10)
                .build();

        Inventario i2 = Inventario.builder()
                .id(2L)
                .productoId(2L)
                .stock(20)
                .build();

        when(inventarioService.findAll())
                .thenReturn(Arrays.asList(i1, i2));

        when(assembler.toModel(any(Inventario.class)))
                .thenReturn(EntityModel.of(new InventarioDTO()));

        mockMvc.perform(get("/inventarios"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarInventarioPorId() throws Exception {

        Inventario inventario = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .stock(10)
                .build();

        when(inventarioService.findById(1L))
                .thenReturn(inventario);

        when(assembler.toModel(any(Inventario.class)))
                .thenReturn(EntityModel.of(new InventarioDTO()));

        mockMvc.perform(get("/inventarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeRetornar404CuandoInventarioNoExiste() throws Exception {

        when(inventarioService.findById(99L))
                .thenReturn(null);

        mockMvc.perform(get("/inventarios/99"))
                .andExpect(status().isNotFound());
    }
}