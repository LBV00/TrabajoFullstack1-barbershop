package com.barbershop.reserva_service.service;

import com.barbershop.reserva_service.model.DetalleReserva;
import com.barbershop.reserva_service.model.Reserva;
import com.barbershop.reserva_service.repository.ReservaRepository;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
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

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final WebClient webClient;

    @Value("${api.user}")
    private String userPath;

    
    public boolean existePorId(Long id) {
        return reservaRepository.existsById(id);
    }

    public ReservaService(ReservaRepository reservaRepository, WebClient webClient) {
        this.reservaRepository = reservaRepository;
        this.webClient = webClient;
    }

    public List<Reserva> findAll() { return reservaRepository.findAll(); }
    
    public Optional<Reserva> findById(Long id) { return reservaRepository.findById(id); }

    public Reserva save(Reserva reserva) {
        log.info("Iniciando validación para crear reserva del Usuario ID: {}", reserva.getIdUsuario());

        Boolean existeUsuario = webClient.get()
                .uri(String.format(userPath, reserva.getIdUsuario()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeUsuario)) {
            log.error("Fallo en la validación: El cliente con ID {} no existe en el sistema.", reserva.getIdUsuario());
            throw new RuntimeException("Error: El cliente ingresado no existe.");
        }

        log.info("Validación exitosa. Procesando y guardando la reserva en la base de datos.");

        if (reserva.getFechaReserva() == null) reserva.setFechaReserva(LocalDateTime.now());
        if (reserva.getEstado() == null) reserva.setEstado("PENDIENTE");

        if (reserva.getDetalles() != null) {
            for (DetalleReserva detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }
        return reservaRepository.save(reserva);
    }

    public void deleteById(Long id) { 
        log.info("Eliminando reserva con ID: {}", id);
        reservaRepository.deleteById(id); 
    }
    
    public List<Reserva> findByIdUsuario(Long idUsuario) {
        return reservaRepository.findByIdUsuario(idUsuario);
    }

    public Long contarTotalReservas() {
        return reservaRepository.count();
    }
     
}