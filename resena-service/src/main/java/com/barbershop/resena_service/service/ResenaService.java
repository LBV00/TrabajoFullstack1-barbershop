package com.barbershop.resena_service.service;

import com.barbershop.resena_service.dto.ResenaDTO;
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

    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);

    private final ResenaRepository resenaRepository;
    private final WebClient webClient;

    @Value("${api.user}")
    private String userPath;

    public ResenaService(ResenaRepository resenaRepository, WebClient webClient) {
        this.resenaRepository = resenaRepository;
        this.webClient = webClient;
    }

    public Resena saveFromDto(ResenaDTO dto) {
        log.info("Iniciando validación para crear reseña del usuario ID: {}", dto.getIdUsuario());

        Boolean existeUsuario = webClient.get()
                .uri(String.format(userPath, dto.getIdUsuario()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeUsuario)) {
            log.error("ERROR: No se pudo crear la reseña. El usuario ID {} no existe.", dto.getIdUsuario());
            throw new RuntimeException("El usuario con ID " + dto.getIdUsuario() + " no existe en el sistema.");
        }

        Resena resena = Resena.builder()
                .idUsuario(dto.getIdUsuario())
                .idReserva(dto.getIdReserva())
                .calificacion(dto.getCalificacion())
                .comentario(dto.getComentario())
                .fechaCreacion(LocalDateTime.now())
                .build();

        Resena guardada = resenaRepository.save(resena);
        log.info("Reseña ID {} creada correctamente para usuario ID {}.", guardada.getId(), dto.getIdUsuario());
        return guardada;
    }

    public Resena actualizar(Long id, ResenaDTO dto) {
        log.info("Actualizando reseña ID: {}", id);
        Resena existente = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña con ID " + id + " no encontrada."));

        existente.setCalificacion(dto.getCalificacion());
        existente.setComentario(dto.getComentario());

        Resena actualizada = resenaRepository.save(existente);
        log.info("Reseña ID {} actualizada correctamente.", id);
        return actualizada;
    }

    public List<Resena> findAll() { return resenaRepository.findAll(); }

    public Optional<Resena> findById(Long id) { return resenaRepository.findById(id); }

    public List<Resena> findByIdUsuario(Long idUsuario) {
        log.info("Buscando historial de reseñas del usuario ID: {}", idUsuario);
        return resenaRepository.findByIdUsuario(idUsuario);
    }

    public void deleteById(Long id) {
        resenaRepository.deleteById(id);
        log.warn("Se ha eliminado la reseña ID: {}", id);
    }

    public Long contarTotalResenas() {
        log.info("Calculando el total de reseñas registradas.");
        return resenaRepository.count();
    }
}