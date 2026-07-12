package com.barbershop.resena_service.service;

import com.barbershop.resena_service.model.Resena;
import com.barbershop.resena_service.repository.ResenaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResenaService — Pruebas unitarias")
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ResenaService resenaService;

    // ------------------------------------------------------------------ //
    //  PRUEBAS CRUD                                                        //
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("Debe listar todas las reseñas")
    void debeListarTodasLasResenas() {
        // Given
        Resena r1 = Resena.builder()
                .id(1L)
                .idUsuario(1L)
                .idReserva(1L)
                .calificacion(5)
                .comentario("Excelente servicio")
                .fechaCreacion(LocalDateTime.now())
                .build();

        Resena r2 = Resena.builder()
                .id(2L)
                .idUsuario(2L)
                .idReserva(2L)
                .calificacion(4)
                .comentario("Muy buen corte")
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(resenaRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        // When
        List<Resena> resultado = resenaService.findAll();

        // Then
        assertEquals(2, resultado.size());
        verify(resenaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar una reseña por ID existente")
    void debeBuscarResenaPorIdExistente() {
        // Given
        Resena resena = Resena.builder()
                .id(1L)
                .idUsuario(1L)
                .idReserva(1L)
                .calificacion(5)
                .comentario("Perfecto")
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));

        // When
        Optional<Resena> resultado = resenaService.findById(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(5, resultado.get().getCalificacion());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío si la reseña no existe")
    void debeRetornarOptionalVacioCuandoResenaNoExiste() {
        // Given
        when(resenaRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Resena> resultado = resenaService.findById(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Debe eliminar una reseña por ID")
    void debeEliminarResena() {
        // Given — (no setup needed)

        // When
        resenaService.deleteById(1L);

        // Then
        verify(resenaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe contar el total de reseñas")
    void debeContarTotalResenas() {
        // Given
        when(resenaRepository.count()).thenReturn(10L);

        // When
        Long total = resenaService.contarTotalResenas();

        // Then
        assertEquals(10L, total);
        verify(resenaRepository, times(1)).count();
    }

}
