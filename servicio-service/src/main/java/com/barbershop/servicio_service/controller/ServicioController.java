package com.barbershop.servicio_service.controller;

import com.barbershop.servicio_service.dto.ServicioDTO;
import com.barbershop.servicio_service.model.Servicio;
import com.barbershop.servicio_service.service.ServicioService;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/servicios")
@Tag(name = "Servicios", description = "Operaciones para administrar servicios")
public class ServicioController {

    private static final Logger logger =
            LoggerFactory.getLogger(ServicioController.class);

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    @Operation(summary = "Listar servicios", description = "Obtiene todos los servicios registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicios encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServicioDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No existen servicios", content = @Content)
    })
    public ResponseEntity<List<ServicioDTO>> getAll() {

        logger.info("Obteniendo todos los servicios");

        List<Servicio> servicios = servicioService.findAll();

        if (servicios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ServicioDTO> dtos = servicios.stream()
                .map(ServicioDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar servicio por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio encontrado",
                    content = @Content(schema = @Schema(implementation = ServicioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<ServicioDTO> getById(
            @Parameter(description = "ID del servicio", example = "1", required = true)
            @PathVariable Long id) {

        logger.info("Buscando servicio {}", id);

        Servicio servicio = servicioService.findById(id);

        if (servicio != null) {
            return ResponseEntity.ok(
                    ServicioDTO.fromModel(servicio));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear servicio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Servicio creado",
                    content = @Content(schema = @Schema(implementation = ServicioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<ServicioDTO> create(
            @Valid @RequestBody ServicioDTO servicioDTO) {

        logger.info("Creando servicio {}", servicioDTO.getNombre());

        Servicio savedServicio =
                servicioService.save(
                        servicioDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ServicioDTO.fromModel(savedServicio));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio actualizado",
                    content = @Content(schema = @Schema(implementation = ServicioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<ServicioDTO> update(
            @Parameter(description = "ID del servicio", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ServicioDTO servicioDTO) {

        logger.info("Actualizando servicio {}", id);

        if (servicioService.findById(id) != null) {

            servicioDTO.setId(id);

            Servicio updatedServicio =
                    servicioService.save(
                            servicioDTO.toModel());

            return ResponseEntity.ok(
                    ServicioDTO.fromModel(updatedServicio));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar servicio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Servicio eliminado"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del servicio", example = "1", required = true)
            @PathVariable Long id) {

        logger.info("Eliminando servicio {}", id);

        if (servicioService.findById(id) != null) {

            servicioService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe un servicio")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existsServicio(
            @Parameter(description = "ID del servicio", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                servicioService.existsById(id));
    }
}