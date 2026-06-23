package com.barbershop.empleado_service.controller;

import com.barbershop.empleado_service.assembler.EmpleadoModelAssembler;
import com.barbershop.empleado_service.dto.EmpleadoDTO;
import com.barbershop.empleado_service.model.Empleado;
import com.barbershop.empleado_service.service.EmpleadoService;

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

@WebMvcTest(EmpleadoController.class)
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private EmpleadoModelAssembler assembler;

    @Test
    void debeListarEmpleados() throws Exception {

        Empleado e1 = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .build();

        Empleado e2 = Empleado.builder()
                .id(2L)
                .nombre("Pedro")
                .build();

        when(empleadoService.findAll())
                .thenReturn(Arrays.asList(e1, e2));

        when(assembler.toModel(any(Empleado.class)))
                .thenReturn(EntityModel.of(new EmpleadoDTO()));

        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarEmpleadoPorId() throws Exception {

        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .build();

        when(empleadoService.findById(1L))
                .thenReturn(empleado);

        when(assembler.toModel(any(Empleado.class)))
                .thenReturn(EntityModel.of(new EmpleadoDTO()));

        mockMvc.perform(get("/empleados/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeRetornar404CuandoEmpleadoNoExiste() throws Exception {

        when(empleadoService.findById(99L))
                .thenReturn(null);

        mockMvc.perform(get("/empleados/99"))
                .andExpect(status().isNotFound());
    }
}