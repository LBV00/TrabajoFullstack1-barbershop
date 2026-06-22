package com.barbershop.resena_service.controller;

import com.barbershop.resena_service.dto.ResenaDTO;
import com.barbershop.resena_service.model.Resena;
import com.barbershop.resena_service.service.ResenaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/resenas")
@Tag(name = "Reseñas", description = "Operaciones para administrar reseñas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    @Operation(summary = "Listar reseñas", description = "Obtiene todas las reseñas registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseñas encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resena.class)))),
            @ApiResponse(responseCode = "204", description = "No existen reseñas", content = @Content)
    })
    public ResponseEntity<List<Resena>> getAll() {

        List<Resena> resenas = resenaService.findAll();

        if (resenas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resenas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reseña por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada",
                    content = @Content(schema = @Schema(implementation = Resena.class))),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    public ResponseEntity<Resena> getById(
            @Parameter(description = "ID de la reseña", example = "1", required = true)
            @PathVariable Long id) {

        Optional<Resena> resena = resenaService.findById(id);

        return resena.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Buscar reseñas por usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
            @ApiResponse(responseCode = "204", description = "No existen reseñas para el usuario")
    })
    public ResponseEntity<List<Resena>> getByIdUsuario(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long idUsuario) {

        List<Resena> resenas = resenaService.findByIdUsuario(idUsuario);

        if (resenas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resenas);
    }

    @GetMapping("/total")
    @Operation(summary = "Obtener cantidad total de reseñas")
    @ApiResponse(responseCode = "200", description = "Total de reseñas")
    public ResponseEntity<Long> getTotalResenas() {

        return ResponseEntity.ok(
                resenaService.contarTotalResenas());
    }

    @PostMapping
    @Operation(summary = "Crear reseña")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada",
                    content = @Content(schema = @Schema(implementation = Resena.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<?> create(
            @Valid @RequestBody ResenaDTO resenaDto) {

        try {

            Resena nueva =
                    resenaService.saveFromDto(resenaDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nueva);

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reseña")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada",
                    content = @Content(schema = @Schema(implementation = Resena.class))),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID de la reseña", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ResenaDTO resenaDto) {

        Optional<Resena> existente =
                resenaService.findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {

            Resena actualizada =
                    resenaService.actualizar(id, resenaDto);

            return ResponseEntity.ok(actualizada);

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la reseña", example = "1", required = true)
            @PathVariable Long id) {

        if (resenaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        resenaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}