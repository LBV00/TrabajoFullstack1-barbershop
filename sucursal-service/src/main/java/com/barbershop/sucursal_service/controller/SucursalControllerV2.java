package com.barbershop.sucursal_service.controller;

import com.barbershop.sucursal_service.dto.SucursalDTO;
import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.service.SucursalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.barbershop.sucursal_service.assembler.SucursalModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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
@RequestMapping("/sucursales/v2")
@Tag(name = "Sucursales", description = "Operaciones para administrar sucursales")
public class SucursalControllerV2 {

    private static final Logger logger =
            LoggerFactory.getLogger(SucursalControllerV2.class);

    private final SucursalService sucursalService;
    private final SucursalModelAssembler assembler;
    public SucursalControllerV2(
        SucursalService sucursalService,
        SucursalModelAssembler assembler) {

    this.sucursalService = sucursalService;
    this.assembler = assembler;
   }
    @GetMapping
    @Operation(summary = "(V2) Listar sucursales")
  public ResponseEntity<CollectionModel<EntityModel<SucursalDTO>>> getAll() {
    List<EntityModel<SucursalDTO>> sucursales =
            sucursalService.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();
    CollectionModel<EntityModel<SucursalDTO>> collection =
            CollectionModel.of(
                    sucursales,
                    linkTo(methodOn(SucursalController.class)
                            .getAll())
                            .withSelfRel()
            );
    return ResponseEntity.ok(collection);
  }

    @GetMapping("/{id}")
    @Operation(summary = "(V2) Buscar sucursal por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
     })
    public ResponseEntity<EntityModel<SucursalDTO>> getById(
        @PathVariable Long id) {

    return ResponseEntity.ok(
            assembler.toModel(
                    sucursalService.findById(id)
            )
      );
     }

@PostMapping
    public ResponseEntity<SucursalDTO> create(@RequestBody SucursalDTO sucursalDTO) {

        logger.info("Creando sucursal {}", sucursalDTO.getNombre());

        Sucursal savedSucursal =
                sucursalService.save(sucursalDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SucursalDTO.fromModel(savedSucursal));
    }


    @Operation(summary = "Actualizar sucursal")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal actualizada",
                    content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    public ResponseEntity<SucursalDTO> update(
            @Parameter(description = "ID de la sucursal", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody SucursalDTO sucursalDTO) {

        if (sucursalService.findById(id) != null) {

            sucursalDTO.setId(id);

            Sucursal updatedSucursal =
                    sucursalService.save(sucursalDTO.toModel());

            return ResponseEntity.ok(
                    SucursalDTO.fromModel(updatedSucursal));
        }

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Eliminar sucursal")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sucursal eliminada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la sucursal", example = "1", required = true)
            @PathVariable Long id) {

        logger.info("Eliminando sucursal {}", id);

        if (sucursalService.findById(id) != null) {

            sucursalService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe una sucursal")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existsSucursal(
            @Parameter(description = "ID de la sucursal", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sucursalService.existsById(id));
    }
}
