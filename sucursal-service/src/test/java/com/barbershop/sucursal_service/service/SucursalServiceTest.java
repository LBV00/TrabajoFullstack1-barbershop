package com.barbershop.sucursal_service.service;

import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.repository.SucursalRepository;

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
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @Test
    void debeListarSucursales() {

        Sucursal s1 = Sucursal.builder()
                .id(1L)
                .nombre("La Reina")
                .direccion("Av. Larraín")
                .telefono("123456")
                .build();

        Sucursal s2 = Sucursal.builder()
                .id(2L)
                .nombre("Providencia")
                .direccion("Av. Providencia")
                .telefono("654321")
                .build();

        when(sucursalRepository.findAll())
                .thenReturn(Arrays.asList(s1, s2));

        List<Sucursal> resultado = sucursalService.findAll();

        assertEquals(2, resultado.size());

        verify(sucursalRepository, times(1))
                .findAll();
    }

    @Test
    void debeBuscarSucursalPorId() {

        Sucursal sucursal = Sucursal.builder()
                .id(1L)
                .nombre("La Reina")
                .build();

        when(sucursalRepository.findById(1L))
                .thenReturn(Optional.of(sucursal));

        Sucursal resultado = sucursalService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void debeGuardarSucursal() {

        Sucursal sucursal = Sucursal.builder()
                .nombre("La Reina")
                .build();

        when(sucursalRepository.save(any(Sucursal.class)))
                .thenReturn(sucursal);

        Sucursal resultado = sucursalService.save(sucursal);

        assertNotNull(resultado);
        assertEquals("La Reina", resultado.getNombre());
    }

    @Test
    void debeEliminarSucursal() {
        when(sucursalRepository.existsById(1L)).thenReturn(true);
        sucursalService.delete(1L);

        verify(sucursalRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void debeVerificarExistenciaSucursal() {

        when(sucursalRepository.existsById(1L))
                .thenReturn(true);

        boolean existe = sucursalService.existsById(1L);

        assertTrue(existe);
    }
}