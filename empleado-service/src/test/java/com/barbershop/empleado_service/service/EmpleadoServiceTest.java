package com.barbershop.empleado_service.service;

import com.barbershop.empleado_service.model.Empleado;
import com.barbershop.empleado_service.repository.EmpleadoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository repository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    void debeListarEmpleados() {

        Empleado e1 = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .build();

        Empleado e2 = Empleado.builder()
                .id(2L)
                .nombre("Pedro")
                .build();

        when(repository.findAll())
                .thenReturn(Arrays.asList(e1, e2));

        List<Empleado> resultado = empleadoService.findAll();

        assertEquals(2, resultado.size());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void debeBuscarEmpleadoPorId() {

        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(empleado));

        Empleado resultado = empleadoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void debeGuardarEmpleado() {

        Empleado empleado = Empleado.builder()
                .nombre("Juan")
                .build();

        when(repository.save(any(Empleado.class)))
                .thenReturn(empleado);

        Empleado resultado = empleadoService.save(empleado);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void debeEliminarEmpleado() {

        empleadoService.delete(1L);

        verify(repository, times(1))
                .deleteById(1L);
    }

    @Test
    void debeVerificarExistenciaEmpleado() {

        when(repository.existsById(1L))
                .thenReturn(true);

        boolean existe = empleadoService.existsById(1L);

        assertTrue(existe);
    }
}