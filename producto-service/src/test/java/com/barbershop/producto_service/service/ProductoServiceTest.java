package com.barbershop.producto_service.service;

import com.barbershop.producto_service.model.Producto;
import com.barbershop.producto_service.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("ProductoService — Pruebas unitarias")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    // ------------------------------------------------------------------ //
    //  PRUEBAS CRUD                                                        //
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("Debe listar todos los productos")
    void debeListarTodosLosProductos() {
        // Given
        Producto p1 = Producto.builder()
                .id(1L)
                .nombre("Cera moldeadora mate")
                .precio(8500.0)
                .stock(20)
                .build();

        Producto p2 = Producto.builder()
                .id(2L)
                .nombre("Aceite para barba")
                .precio(12000.0)
                .stock(15)
                .build();

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // When
        List<Producto> resultado = productoService.findAll();

        // Then
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar producto por ID existente")
    void debeBuscarProductoPorIdExistente() {
        // Given
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Cera moldeadora mate")
                .precio(8500.0)
                .stock(20)
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> resultado = productoService.findById(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Cera moldeadora mate", resultado.get().getNombre());
    }

    @Test
    @DisplayName("Debe guardar un producto")
    void debeGuardarProducto() {
        // Given
        Producto producto = Producto.builder()
                .nombre("Peine clásico")
                .precio(4000.0)
                .stock(40)
                .build();

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        Producto resultado = productoService.save(producto);

        // Then
        assertNotNull(resultado);
        assertEquals("Peine clásico", resultado.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe eliminar un producto por ID")
    void debeEliminarProducto() {
        // Given — (no setup needed)

        // When
        productoService.eliminarPorId(1L);

        // Then
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe verificar existencia de producto")
    void debeVerificarExistenciaProducto() {
        // Given
        when(productoRepository.existsById(1L)).thenReturn(true);

        // When
        Boolean existe = productoService.existsById(1L);

        // Then
        assertTrue(existe);
        verify(productoRepository, times(1)).existsById(1L);
    }

}
