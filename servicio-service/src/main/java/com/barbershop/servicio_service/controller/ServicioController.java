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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/servicios")
@Tag(name = "Servicios", description = "Operaciones para administrar servicios")
public class ServicioController {

    private static final Logger logger =
            LoggerFactory.getLogger(ServicioController.class);

    private final ServicioService servicioService;
    

    public ServicioController(
        ServicioService servicioService) {

    this.servicioService = servicioService;
    
    }

    @GetMapping
    @Operation(summary = "(V1) Listar servicios")
            public ResponseEntity<List<ServicioDTO>> getAll() {
        List<ServicioDTO> lista = servicioService.findAll()
                .stream()
                .map(ServicioDTO::fromModel)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "(V1) Buscar servicio por ID")
            public ResponseEntity<ServicioDTO> getById(@PathVariable Long id) {
        Servicio model = servicioService.findById(id);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ServicioDTO.fromModel(model));
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