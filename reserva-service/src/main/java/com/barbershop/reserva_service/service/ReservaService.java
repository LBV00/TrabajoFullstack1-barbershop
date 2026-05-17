package com.barbershop.reserva_service.service;

import com.barbershop.reserva_service.model.DetalleReserva;
import com.barbershop.reserva_service.model.Reserva;
import com.barbershop.reserva_service.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final WebClient webClient;

    @Value("${api.user}")
    private String userPath;

    public ReservaService(ReservaRepository reservaRepository, WebClient webClient) {
        this.reservaRepository = reservaRepository;
        this.webClient = webClient;
    }

    public List<Reserva> findAll() { return reservaRepository.findAll(); }
    
    public Optional<Reserva> findById(Long id) { return reservaRepository.findById(id); }

    public Reserva save(Reserva reserva) {
        // Validación de comunicación: Consultar al user-service (a través del Gateway)
        Boolean existeUsuario = webClient.get()
                .uri(String.format(userPath, reserva.getIdUsuario()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeUsuario)) {
            throw new RuntimeException("Error: El cliente ingresado no existe.");
        }

        if (reserva.getFechaReserva() == null) reserva.setFechaReserva(LocalDateTime.now());
        if (reserva.getEstado() == null) reserva.setEstado("PENDIENTE");

        // Amarrar los detalles a la cabecera
        if (reserva.getDetalles() != null) {
            for (DetalleReserva detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }
        return reservaRepository.save(reserva);
    }

    public void deleteById(Long id) { reservaRepository.deleteById(id); }
    
    // Nuevo endpoint 1: Búsqueda por Usuario
    public List<Reserva> findByIdUsuario(Long idUsuario) {
        return reservaRepository.findByIdUsuario(idUsuario);
    }

    // Nuevo endpoint 2: Total de reservas
    public Long contarTotalReservas() {
        return reservaRepository.count();
    }
     
}