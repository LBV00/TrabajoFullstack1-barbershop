package com.barbershop.servicio_service.controller;

import com.barbershop.servicio_service.dto.ServicioDTO;
import com.barbershop.servicio_service.model.Servicio;
import com.barbershop.servicio_service.service.ServicioService;
import com.barbershop.servicio_service.assembler.ServicioModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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
@RequestMapping("/servicios/v2")
@Tag(name = "Servicios", description = "Operaciones para administrar servicios")
public class ServicioControllerV2 {

    private static final Logger logger =
            LoggerFactory.getLogger(ServicioControllerV2.class);

    private final ServicioService servicioService;
    private final ServicioModelAssembler assembler;

    public ServicioControllerV2(
        ServicioService servicioService,
        ServicioModelAssembler assembler) {

    this.servicioService = servicioService;
    this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "(V2) Listar servicios")
        public ResponseEntity<CollectionModel<EntityModel<ServicioDTO>>> getAll() {
    List<EntityModel<ServicioDTO>> servicios =
            servicioService.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();
    CollectionModel<EntityModel<ServicioDTO>> collection =
            CollectionModel.of(
                    servicios,
                    linkTo(methodOn(ServicioController.class)
                            .getAll())
                            .withSelfRel()
            );
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "(V2) Buscar servicio por ID")
        public ResponseEntity<EntityModel<ServicioDTO>> getById(
        @PathVariable Long id) {
    Servicio servicio = servicioService.findById(id);
    if (servicio == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(
            assembler.toModel(servicio)
    );
   } 

@PostMapping
    public ResponseEntity<ServicioDTO> create(
            @Valid @RequestBody ServicioDTO servicioDTO) {

        logger.info("Creando servicio {}", servicioDTO.getNombre());

        Servicio savedServicio =
                servicioService.save(
                        servicioDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ServicioDTO.fromModel(savedServicio));
    }


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
