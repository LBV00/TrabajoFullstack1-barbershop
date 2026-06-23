package com.barbershop.servicio_service.controller;

import com.barbershop.servicio_service.assembler.ServicioModelAssembler;
import com.barbershop.servicio_service.dto.ServicioDTO;
import com.barbershop.servicio_service.model.Servicio;
import com.barbershop.servicio_service.service.ServicioService;

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

@WebMvcTest(ServicioController.class)
class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioService servicioService;

    @MockBean
    private ServicioModelAssembler assembler;

    @Test
    void debeListarServicios() throws Exception {

        Servicio s1 = Servicio.builder()
                .id(1L)
                .nombre("Corte Fade")
                .build();

        Servicio s2 = Servicio.builder()
                .id(2L)
                .nombre("Barba Premium")
                .build();

        when(servicioService.findAll())
                .thenReturn(Arrays.asList(s1, s2));

        when(assembler.toModel(any(Servicio.class)))
                .thenReturn(EntityModel.of(new ServicioDTO()));

        mockMvc.perform(get("/servicios"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarServicioPorId() throws Exception {

        Servicio servicio = Servicio.builder()
                .id(1L)
                .nombre("Corte Fade")
                .build();

        when(servicioService.findById(1L))
                .thenReturn(servicio);

        when(assembler.toModel(any(Servicio.class)))
                .thenReturn(EntityModel.of(new ServicioDTO()));

        mockMvc.perform(get("/servicios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeRetornar404CuandoServicioNoExiste() throws Exception {

        when(servicioService.findById(99L))
                .thenReturn(null);

        mockMvc.perform(get("/servicios/99"))
                .andExpect(status().isNotFound());
    }
}