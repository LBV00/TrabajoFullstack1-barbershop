package com.barbershop.reserva_service.service;

import com.barbershop.reserva_service.model.Reserva;
import com.barbershop.reserva_service.repository.ReservaRepository;

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

/**
 * Pruebas unitarias aisladas para ReservaService.
 * No usa base de datos; el repositorio y WebClient se simulan con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ReservaService reservaService;

    // ── findAll ─────────────────────────────────────────────────────────────

    @Test
    void debeListarTodasLasReservas() {

        Reserva r1 = Reserva.builder()
                .id(1L)
                .idUsuario(10L)
                .fechaReserva(LocalDateTime.now())
                .total(15000.0)
                .estado("PENDIENTE")
                .build();

        Reserva r2 = Reserva.builder()
                .id(2L)
                .idUsuario(11L)
                .fechaReserva(LocalDateTime.now())
                .total(20000.0)
                .estado("CONFIRMADA")
                .build();

        when(reservaRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Reserva> resultado = reservaService.findAll();

        assertEquals(2, resultado.size());
        verify(reservaRepository, times(1)).findAll();
    }

    // ── findById ─────────────────────────────────────────────────────────────

    @Test
    void debeBuscarReservaPorId() {

        Reserva reserva = Reserva.builder()
                .id(1L)
                .idUsuario(10L)
                .total(15000.0)
                .estado("PENDIENTE")
                .build();

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Optional<Reserva> resultado = reservaService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void debeRetornarVacioSiReservaNoExiste() {

        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Reserva> resultado = reservaService.findById(99L);

        assertFalse(resultado.isPresent());
    }

    // ── deleteById ───────────────────────────────────────────────────────────

    @Test
    void debeEliminarReservaPorId() {

        reservaService.deleteById(1L);

        verify(reservaRepository, times(1)).deleteById(1L);
    }

    // ── existePorId ──────────────────────────────────────────────────────────

    @Test
    void debeVerificarExistenciaDeReserva() {

        when(reservaRepository.existsById(1L)).thenReturn(true);

        boolean existe = reservaService.existePorId(1L);

        assertTrue(existe);
        verify(reservaRepository, times(1)).existsById(1L);
    }

    @Test
    void debeRetornarFalseSiReservaNoExiste() {

        when(reservaRepository.existsById(99L)).thenReturn(false);

        boolean existe = reservaService.existePorId(99L);

        assertFalse(existe);
    }

    // ── findByIdUsuario ───────────────────────────────────────────────────────

    @Test
    void debeListarReservasPorUsuario() {

        Reserva r1 = Reserva.builder().id(1L).idUsuario(10L).total(15000.0).estado("PENDIENTE").build();
        Reserva r2 = Reserva.builder().id(2L).idUsuario(10L).total(20000.0).estado("CONFIRMADA").build();

        when(reservaRepository.findByIdUsuario(10L)).thenReturn(Arrays.asList(r1, r2));

        List<Reserva> resultado = reservaService.findByIdUsuario(10L);

        assertEquals(2, resultado.size());
        resultado.forEach(r -> assertEquals(10L, r.getIdUsuario()));
        verify(reservaRepository, times(1)).findByIdUsuario(10L);
    }

    // ── contarTotalReservas ──────────────────────────────────────────────────

    @Test
    void debeContarTotalDeReservas() {

        when(reservaRepository.count()).thenReturn(5L);

        Long total = reservaService.contarTotalReservas();

        assertEquals(5L, total);
        verify(reservaRepository, times(1)).count();
    }
}
