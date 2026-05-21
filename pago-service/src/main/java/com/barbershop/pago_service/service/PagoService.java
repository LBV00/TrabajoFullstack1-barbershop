package com.barbershop.pago_service.service;

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

    // ---> AGREGADO: Instancia del Logger <---
    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;
    private final WebClient webClient;

    // Rutas de validación conectadas al API Gateway (7090)
    @Value("${api.usuario.exists:http://localhost:7090/users/%d/exists}")
    private String usuarioPath;

    @Value("${api.reserva.exists:http://localhost:7090/reservas/%d/exists}")
    private String reservaPath;

    public PagoService(PagoRepository pagoRepository, WebClient webClient) {
        this.pagoRepository = pagoRepository;
        this.webClient = webClient;
    }

    public Pago guardar(Pago pago) {
        // ---> AGREGADO: Log de inicio <---
        log.info("Iniciando validación de pago para Usuario ID: {} y Reserva ID: {}", pago.getIdUsuario(), pago.getIdReserva());

        // 1. Comunicación con el microservicio de Usuarios
        Boolean existeUsuario = webClient.get()
                .uri(String.format(usuarioPath, pago.getIdUsuario()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block(); // .block() hace que la petición sea síncrona

        if (Boolean.FALSE.equals(existeUsuario)) {
            // ---> AGREGADO: Log de error <---
            log.error("Fallo en la validación: El usuario con ID {} no existe.", pago.getIdUsuario());
            throw new RuntimeException("ERROR: El usuario con ID " + pago.getIdUsuario() + " no existe.");
        }

        // 2. Comunicación con el microservicio de Reservas
        Boolean existeReserva = webClient.get()
                .uri(String.format(reservaPath, pago.getIdReserva()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeReserva)) {
            // ---> AGREGADO: Log de error <---
            log.error("Fallo en la validación: La reserva con ID {} no existe.", pago.getIdReserva());
            throw new RuntimeException("ERROR: La reserva con ID " + pago.getIdReserva() + " no existe.");
        }

        // ---> AGREGADO: Log de éxito <---
        log.info("Validaciones exitosas. Procesando el pago en la base de datos.");

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

    // Método para eliminar un pago por su ID
    public void eliminarPorId(Long id) {
        // ---> AGREGADO: Log de eliminación <---
        log.info("Eliminando pago con ID: {}", id);
        pagoRepository.deleteById(id);
    }
    
    // 1. Búsqueda por atributo distinto al ID (por Usuario)
    public List<Pago> findByIdUsuario(Long idUsuario) {
        return pagoRepository.findByIdUsuario(idUsuario); 
    }

    // 2. Totales
    public Long contarTotalPagos() {
        return pagoRepository.count(); // count() viene incluido en JpaRepository
    }
}