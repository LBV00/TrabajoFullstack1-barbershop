package com.barbershop.pago_service.service;

import com.barbershop.pago_service.exception.ResourceNotFoundException;
import com.barbershop.pago_service.model.Pago;
import com.barbershop.pago_service.repository.PagoRepository;
import org.slf4j.Logger; // <-- AGREGADO
import org.slf4j.LoggerFactory; // <-- AGREGADO
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

    
    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;
    private final WebClient webClient;

    
    @Value("${api.usuario.exists:http://localhost:7090/users/%d/exists}")
    private String usuarioPath;

    @Value("${api.reserva.exists:http://localhost:7090/reservas/%d/exists}")
    private String reservaPath;

    public PagoService(PagoRepository pagoRepository, WebClient webClient) {
        this.pagoRepository = pagoRepository;
        this.webClient = webClient;
    }

    public Pago guardar(Pago pago) {
        
        log.info("Iniciando validación de pago para Usuario ID: {} y Reserva ID: {}", pago.getIdUsuario(), pago.getIdReserva());

        Boolean existeUsuario = webClient.get()
                .uri(String.format(usuarioPath, pago.getIdUsuario()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block(); 
        if (Boolean.FALSE.equals(existeUsuario)) {
            
            log.error("Fallo en la validación: El usuario con ID {} no existe.", pago.getIdUsuario());
            throw new ResourceNotFoundException("El usuario con ID " + pago.getIdUsuario() + " no existe.");
        }

        Boolean existeReserva = webClient.get()
                .uri(String.format(reservaPath, pago.getIdReserva()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeReserva)) {
            
            log.error("Fallo en la validación: La reserva con ID {} no existe.", pago.getIdReserva());
            throw new ResourceNotFoundException("ERROR: La reserva con ID " + pago.getIdReserva() + " no existe.");
        }

        
        log.info("Validaciones exitosas. Procesando el pago en la base de datos.");

        
        pago.setFechaPago(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    
    public void eliminarPorId(Long id) {
        
        log.info("Eliminando pago con ID: {}", id);
        pagoRepository.deleteById(id);
    }
    
    
    public List<Pago> findByIdUsuario(Long idUsuario) {
        return pagoRepository.findByIdUsuario(idUsuario); 
    }

    
    public Long contarTotalPagos() {
        return pagoRepository.count();
    }
}