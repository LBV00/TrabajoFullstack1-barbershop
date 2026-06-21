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
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sucursales")
@Tag(name = "Sucursales", description = "Operaciones para administrar sucursales")
public class SucursalController {

    private static final Logger logger =
            LoggerFactory.getLogger(SucursalController.class);

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Listar sucursales", description = "Obtiene todas las sucursales registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursales encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SucursalDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No existen sucursales", content = @Content)
    })
    public ResponseEntity<List<SucursalDTO>> getAll() {

        logger.info("Obteniendo todas las sucursales");

        List<Sucursal> sucursales = sucursalService.findAll();

        if (sucursales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<SucursalDTO> dtos = sucursales.stream()
                .map(SucursalDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sucursal por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada",
                    content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    public ResponseEntity<SucursalDTO> getById(
            @Parameter(description = "ID de la sucursal", example = "1", required = true)
            @PathVariable Long id) {

        logger.info("Buscando sucursal {}", id);

        Sucursal sucursal = sucursalService.findById(id);

        if (sucursal != null) {
            return ResponseEntity.ok(SucursalDTO.fromModel(sucursal));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear sucursal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sucursal creada",
                    content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<SucursalDTO> create(@RequestBody SucursalDTO sucursalDTO) {

        logger.info("Creando sucursal {}", sucursalDTO.getNombre());

        Sucursal savedSucursal =
                sucursalService.save(sucursalDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SucursalDTO.fromModel(savedSucursal));
    }

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
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