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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.barbershop.notificacion_service.assembler.NotificacionModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones para administrar notificaciones")
public class NotificacionController {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificacionController.class);

    private final NotificacionService notificacionService;
    private final NotificacionModelAssembler assembler;
    public NotificacionController(
        NotificacionService notificacionService,
        NotificacionModelAssembler assembler) {

    this.notificacionService = notificacionService;
    this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar notificaciones")
    public ResponseEntity<CollectionModel<EntityModel<NotificacionDTO>>> getAll() {
    List<EntityModel<NotificacionDTO>> notificaciones =
            notificacionService.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();
    CollectionModel<EntityModel<NotificacionDTO>> collection =
            CollectionModel.of(
                    notificaciones,
                    linkTo(methodOn(NotificacionController.class)
                            .getAll())
                            .withSelfRel()
            );
    return ResponseEntity.ok(collection);
   }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar notificación por ID")
    public ResponseEntity<EntityModel<NotificacionDTO>> getById(
        @PathVariable Long id) {Notificacion notificacion = notificacionService.findById(id);
    if (notificacion == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(
            assembler.toModel(notificacion)
    );
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