package com.barbershop.resena_service.service;

import com.barbershop.resena_service.model.Resena;
import com.barbershop.resena_service.repository.ResenaRepository;
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
public class ResenaService {

    // Inicializamos el Logger 
    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);

    private final ResenaRepository resenaRepository;
    private final WebClient webClient;

    @Value("${api.user}")
    private String userPath;

    public ResenaService(ResenaRepository resenaRepository, WebClient webClient) {
        this.resenaRepository = resenaRepository;
        this.webClient = webClient;
    }

    public Resena save(Resena resena) {
        log.info("Iniciando validación para crear reseña del cliente ID: {}", resena.getIdCliente());

        // Consultar si el usuario existe usando WebClient
        Boolean existeCliente = webClient.get()
                .uri(String.format(userPath, resena.getIdCliente()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeCliente)) {
            log.error("ERROR: No se pudo crear la reseña. El cliente ID {} no existe.", resena.getIdCliente());
            throw new RuntimeException("El cliente no existe en la base de datos.");
        }

        resena.setFechaCreacion(LocalDateTime.now());
        Resena guardada = resenaRepository.save(resena);
        log.info("¡Éxito! Reseña ID {} guardada correctamente.", guardada.getId());
        return guardada;
    }

    public List<Resena> findAll() { return resenaRepository.findAll(); }

    public Optional<Resena> findById(Long id) { return resenaRepository.findById(id); }

    public List<Resena> findByIdCliente(Long idCliente) {
        log.info("Buscando el historial de reseñas del cliente ID: {}", idCliente);
        return resenaRepository.findByIdCliente(idCliente);
    }

    public void deleteById(Long id) {
        resenaRepository.deleteById(id);
        log.warn("Se ha eliminado la reseña ID: {}", id);
    }
    // Nuevo endpoint para contar el total
    public Long contarTotalResenas() {
        log.info("Calculando el total de reseñas registradas");
        return resenaRepository.count(); // Spring Data JPA trae count() por defecto
    }
    
}