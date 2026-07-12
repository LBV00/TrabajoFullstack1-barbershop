package com.barbershop.servicio_service.service;

import com.barbershop.servicio_service.model.Servicio;
import com.barbershop.servicio_service.repository.ServicioRepository;

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
class ServicioServiceTest {

    @Mock
    private ServicioRepository repository;

    @InjectMocks
    private ServicioService servicioService;

    @Test
    void debeListarServicios() {

        Servicio s1 = Servicio.builder()
                .id(1L)
                .nombre("Corte Fade")
                .build();

        Servicio s2 = Servicio.builder()
                .id(2L)
                .nombre("Barba Premium")
                .build();

        when(repository.findAll())
                .thenReturn(Arrays.asList(s1, s2));

        List<Servicio> resultado = servicioService.findAll();

        assertEquals(2, resultado.size());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void debeBuscarServicioPorId() {

        Servicio servicio = Servicio.builder()
                .id(1L)
                .nombre("Corte Fade")
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(servicio));

        Servicio resultado = servicioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void debeGuardarServicio() {

        Servicio servicio = Servicio.builder()
                .nombre("Corte Fade")
                .build();

        when(repository.save(any(Servicio.class)))
                .thenReturn(servicio);

        Servicio resultado = servicioService.save(servicio);

        assertNotNull(resultado);
        assertEquals("Corte Fade", resultado.getNombre());
    }

    @Test
    void debeEliminarServicio() {
        when(repository.existsById(1L)).thenReturn(true);
        servicioService.delete(1L);

        verify(repository, times(1))
                .deleteById(1L);
    }

    @Test
    void debeVerificarExistenciaServicio() {

        when(repository.existsById(1L))
                .thenReturn(true);

        boolean existe = servicioService.existsById(1L);

        assertTrue(existe);
    }
}