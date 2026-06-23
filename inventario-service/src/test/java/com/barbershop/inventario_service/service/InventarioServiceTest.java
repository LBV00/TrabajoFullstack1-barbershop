package com.barbershop.inventario_service.service;

import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.repository.InventarioRepository;

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
class InventarioServiceTest {

    @Mock
    private InventarioRepository repository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void debeListarInventarios() {

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

        when(repository.findAll())
                .thenReturn(Arrays.asList(i1, i2));

        List<Inventario> resultado = inventarioService.findAll();

        assertEquals(2, resultado.size());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void debeBuscarInventarioPorId() {

        Inventario inventario = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .stock(10)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void debeGuardarInventario() {

        Inventario inventario = Inventario.builder()
                .productoId(1L)
                .stock(10)
                .build();

        when(repository.save(any(Inventario.class)))
                .thenReturn(inventario);

        Inventario resultado = inventarioService.save(inventario);

        assertNotNull(resultado);
        assertEquals(10, resultado.getStock());
    }

    @Test
    void debeEliminarInventario() {

        inventarioService.delete(1L);

        verify(repository, times(1))
                .deleteById(1L);
    }

    @Test
    void debeVerificarExistenciaInventario() {

        when(repository.existsById(1L))
                .thenReturn(true);

        boolean existe = inventarioService.existsById(1L);

        assertTrue(existe);
    }
}