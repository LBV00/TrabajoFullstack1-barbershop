package com.barbershop.notificacion_service.controller;

import com.barbershop.notificacion_service.dto.NotificacionDTO;
import com.barbershop.notificacion_service.model.Notificacion;
import com.barbershop.notificacion_service.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones para administrar notificaciones")
public class NotificacionController {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificacionController.class);

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    @Operation(summary = "Listar notificaciones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificaciones encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificacionDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No existen notificaciones", content = @Content)
    })
    public ResponseEntity<List<NotificacionDTO>> getAll() {

        logger.info("Obteniendo notificaciones");

        List<Notificacion> notificaciones = notificacionService.findAll();

        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<NotificacionDTO> dtos = notificaciones.stream()
                .map(NotificacionDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar notificación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<NotificacionDTO> getById(
            @Parameter(description = "ID de la notificación", example = "1", required = true)
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
    @Operation(summary = "Crear notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<NotificacionDTO> create(
            @Valid @RequestBody NotificacionDTO dto) {

        Notificacion saved =
                notificacionService.save(dto.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NotificacionDTO.fromModel(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación actualizada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<NotificacionDTO> update(
            @Parameter(description = "ID de la notificación", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody NotificacionDTO dto) {

        if (notificacionService.findById(id) != null) {

            dto.setId(id);

            Notificacion updated =
                    notificacionService.save(dto.toModel());

            return ResponseEntity.ok(
                    NotificacionDTO.fromModel(updated));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificación eliminada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la notificación", example = "1", required = true)
            @PathVariable Long id) {

        if (notificacionService.findById(id) != null) {

            notificacionService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe una notificación")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "ID de la notificación", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                notificacionService.existsById(id));
    }
}