package com.barbershop.notificacion_service.service;

import com.barbershop.notificacion_service.exception.ResourceNotFoundException;
import com.barbershop.notificacion_service.model.Notificacion;
import com.barbershop.notificacion_service.repository.NotificacionRepository;

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

/**
 * Pruebas unitarias aisladas para NotificacionService.
 * No usa base de datos; el repositorio se simula con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService notificacionService;

    // ── findAll ─────────────────────────────────────────────────────────────

    @Test
    void debeListarTodasLasNotificaciones() {

        Notificacion n1 = Notificacion.builder()
                .id(1L)
                .destinatario("cliente@email.com")
                .mensaje("Su reserva fue confirmada")
                .estado("ENVIADA")
                .build();

        Notificacion n2 = Notificacion.builder()
                .id(2L)
                .destinatario("otro@email.com")
                .mensaje("Su reserva fue cancelada")
                .estado("PENDIENTE")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(n1, n2));

        List<Notificacion> resultado = notificacionService.findAll();

        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // ── findById ─────────────────────────────────────────────────────────────

    @Test
    void debeBuscarNotificacionPorId() {

        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .destinatario("cliente@email.com")
                .mensaje("Su reserva fue confirmada")
                .estado("ENVIADA")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));

        Notificacion resultado = notificacionService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("cliente@email.com", resultado.getDestinatario());
    }

    @Test
    void debeLanzarExcepcionSiNotificacionNoExiste() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> notificacionService.findById(99L));
    }

    // ── save ─────────────────────────────────────────────────────────────────

    @Test
    void debeGuardarNotificacion() {

        Notificacion notificacion = Notificacion.builder()
                .destinatario("cliente@email.com")
                .mensaje("Su reserva fue confirmada")
                .estado("PENDIENTE")
                .build();

        when(repository.save(any(Notificacion.class))).thenReturn(notificacion);

        Notificacion resultado = notificacionService.save(notificacion);

        assertNotNull(resultado);
        assertEquals("cliente@email.com", resultado.getDestinatario());
        verify(repository, times(1)).save(notificacion);
    }

    // ── delete ───────────────────────────────────────────────────────────────

    @Test
    void debeEliminarNotificacionExistente() {

        when(repository.existsById(1L)).thenReturn(true);

        notificacionService.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void debeLanzarExcepcionAlEliminarNotificacionInexistente() {

        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> notificacionService.delete(99L));

        verify(repository, never()).deleteById(anyLong());
    }

    // ── existsById ───────────────────────────────────────────────────────────

    @Test
    void debeVerificarExistenciaDeNotificacion() {

        when(repository.existsById(1L)).thenReturn(true);

        boolean existe = notificacionService.existsById(1L);

        assertTrue(existe);
        verify(repository, times(1)).existsById(1L);
    }

    @Test
    void debeRetornarFalseSiNotificacionNoExiste() {

        when(repository.existsById(99L)).thenReturn(false);

        boolean existe = notificacionService.existsById(99L);

        assertFalse(existe);
    }
}
