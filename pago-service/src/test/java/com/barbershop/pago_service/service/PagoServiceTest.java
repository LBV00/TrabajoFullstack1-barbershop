package com.barbershop.pago_service.service;

import com.barbershop.pago_service.exception.BadRequestException;
import com.barbershop.pago_service.model.Pago;
import com.barbershop.pago_service.repository.PagoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService — Pruebas unitarias")
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private PagoService pagoService;

    // ------------------------------------------------------------------ //
    //  PRUEBAS CRUD                                                        //
    // ------------------------------------------------------------------ //

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void mockWebClient() {
        org.springframework.test.util.ReflectionTestUtils.setField(pagoService, "usuarioPath", "http://localhost:7090/users/%d/exists");
        org.springframework.test.util.ReflectionTestUtils.setField(pagoService, "reservaPath", "http://localhost:7090/reservas/%d/exists");
        
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el monto es <= 0")
    void debeLanzarExcepcionMontoInvalido() {
        mockWebClient();
        Pago pago = Pago.builder().idUsuario(1L).idReserva(1L).monto(0.0).metodoPago("TARJETA").build();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> pagoService.guardar(pago));
        assertEquals("El monto del pago debe ser mayor a 0", ex.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el metodo de pago no es valido")
    void debeLanzarExcepcionMetodoPagoInvalido() {
        mockWebClient();
        Pago pago = Pago.builder().idUsuario(1L).idReserva(1L).monto(100.0).metodoPago("BITCOIN").build();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> pagoService.guardar(pago));
        assertEquals("Método de pago no válido", ex.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si la reserva ya fue pagada")
    void debeLanzarExcepcionReservaYaPagada() {
        mockWebClient();
        Pago pago = Pago.builder().idUsuario(1L).idReserva(1L).monto(100.0).metodoPago("EFECTIVO").build();
        when(pagoRepository.existsByIdReserva(1L)).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> pagoService.guardar(pago));
        assertEquals("La reserva ya ha sido pagada previamente", ex.getMessage());
    }

    @Test
    @DisplayName("Debe guardar exitosamente si pasa todas las validaciones")
    void debeGuardarExitosamente() {
        mockWebClient();
        Pago pago = Pago.builder().idUsuario(1L).idReserva(1L).monto(100.0).metodoPago("EFECTIVO").build();
        when(pagoRepository.existsByIdReserva(1L)).thenReturn(false);
        
        Pago savedPago = Pago.builder().id(1L).idUsuario(1L).idReserva(1L).monto(100.0).metodoPago("EFECTIVO").build();
        when(pagoRepository.save(any(Pago.class))).thenReturn(savedPago);

        Pago result = pagoService.guardar(pago);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe listar todos los pagos")
    void debeListarTodosLosPagos() {
        // Given
        Pago p1 = Pago.builder()
                .id(1L)
                .idUsuario(1L)
                .idReserva(1L)
                .monto(15000.0)
                .metodoPago("TARJETA")
                .fechaPago(LocalDateTime.now())
                .build();

        Pago p2 = Pago.builder()
                .id(2L)
                .idUsuario(2L)
                .idReserva(2L)
                .monto(8000.0)
                .metodoPago("EFECTIVO")
                .fechaPago(LocalDateTime.now())
                .build();

        when(pagoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // When
        List<Pago> resultado = pagoService.listar();

        // Then
        assertEquals(2, resultado.size());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un pago por ID existente")
    void debeBuscarPagoPorIdExistente() {
        // Given
        Pago pago = Pago.builder()
                .id(1L)
                .idUsuario(1L)
                .idReserva(1L)
                .monto(15000.0)
                .metodoPago("TRANSFERENCIA")
                .fechaPago(LocalDateTime.now())
                .build();

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        // When
        Optional<Pago> resultado = pagoService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals(15000.0, resultado.get().getMonto());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío si el pago no existe")
    void debeRetornarOptionalVacioCuandoPagoNoExiste() {
        // Given
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Pago> resultado = pagoService.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Debe eliminar un pago por ID")
    void debeEliminarPago() {
        // Given — (no setup needed)

        // When
        pagoService.eliminarPorId(1L);

        // Then
        verify(pagoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe contar el total de pagos")
    void debeContarTotalPagos() {
        // Given
        when(pagoRepository.count()).thenReturn(3L);

        // When
        Long total = pagoService.contarTotalPagos();

        // Then
        assertEquals(3L, total);
        verify(pagoRepository, times(1)).count();
    }

}
