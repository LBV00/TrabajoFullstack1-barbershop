package com.barbershop.notificacion_service.controller;

import com.barbershop.notificacion_service.dto.NotificacionDTO;
import com.barbershop.notificacion_service.model.Notificacion;
import com.barbershop.notificacion_service.service.NotificacionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificacionController.class);

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> getAll() {

        logger.info("Obteniendo notificaciones");

        List<Notificacion> notificaciones = notificacionService.findAll();

        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<NotificacionDTO> dtos =
                notificaciones.stream()
                        .map(NotificacionDTO::fromModel)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> getById(
            @PathVariable Long id) {
                Notificacion notificacion =
                notificacionService.findById(id);

        if (notificacion != null) {
            return ResponseEntity.ok(
                    NotificacionDTO.fromModel(notificacion));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<NotificacionDTO> create(
            @RequestBody NotificacionDTO dto) {

        Notificacion saved =
                notificacionService.save(
                        dto.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NotificacionDTO.fromModel(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacionDTO> update(
            @PathVariable Long id,
            @RequestBody NotificacionDTO dto) {

        if (notificacionService.findById(id) != null) {

            dto.setId(id);

            Notificacion updated =
                    notificacionService.save(
                            dto.toModel());

            return ResponseEntity.ok(
                    NotificacionDTO.fromModel(updated));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (notificacionService.findById(id) != null) {

            notificacionService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                notificacionService.existsById(id));
    }
}