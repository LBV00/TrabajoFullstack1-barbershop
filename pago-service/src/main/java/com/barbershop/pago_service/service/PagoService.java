package com.barbershop.pago_service.service;

import com.barbershop.pago_service.model.Pago;
import com.barbershop.pago_service.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final WebClient webClient;

    // Rutas de validación conectadas al API Gateway (9090)
    @Value("${api.usuario.exists:http://localhost:9090/usuarios/%d/exists}")
    private String usuarioPath;

    @Value("${api.reserva.exists:http://localhost:9090/reservas/%d/exists}")
    private String reservaPath;

    public PagoService(PagoRepository pagoRepository, WebClient webClient) {
        this.pagoRepository = pagoRepository;
        this.webClient = webClient;
    }

    public Pago guardar(Pago pago) {
        // 1. Comunicación con el microservicio de Usuarios
        Boolean existeUsuario = webClient.get()
                .uri(String.format(usuarioPath, pago.getIdUsuario()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block(); // .block() hace que la petición sea síncrona

        if (Boolean.FALSE.equals(existeUsuario)) {
            throw new RuntimeException("ERROR: El usuario con ID " + pago.getIdUsuario() + " no existe.");
        }

        // 2. Comunicación con el microservicio de Reservas
        Boolean existeReserva = webClient.get()
                .uri(String.format(reservaPath, pago.getIdReserva()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeReserva)) {
            throw new RuntimeException("ERROR: La reserva con ID " + pago.getIdReserva() + " no existe.");
        }

        // Si ambos existen, asignamos la fecha y guardamos el pago en la BD
        pago.setFechaPago(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }
}