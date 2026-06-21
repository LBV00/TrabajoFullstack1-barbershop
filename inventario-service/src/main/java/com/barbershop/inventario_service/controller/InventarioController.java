package com.barbershop.inventario_service.controller;

import com.barbershop.inventario_service.dto.InventarioDTO;
import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.service.InventarioService;

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
@RequestMapping("/inventarios")
@Tag(name = "Inventario", description = "Operaciones para administrar inventario")
public class InventarioController {

    private static final Logger logger =
            LoggerFactory.getLogger(InventarioController.class);

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Listar inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario encontrado",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InventarioDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No existe inventario", content = @Content)
    })
    public ResponseEntity<List<InventarioDTO>> getAll() {

        logger.info("Obteniendo inventario");

        List<Inventario> inventarios = inventarioService.findAll();

        if (inventarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<InventarioDTO> dtos = inventarios.stream()
                .map(InventarioDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inventario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<InventarioDTO> getById(
            @Parameter(description = "ID del inventario", example = "1", required = true)
            @PathVariable Long id) {

        Inventario inventario =
                inventarioService.findById(id);

        if (inventario != null) {
            return ResponseEntity.ok(
                    InventarioDTO.fromModel(inventario));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear registro de inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Inventario creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<InventarioDTO> create(
            @Valid @RequestBody InventarioDTO inventarioDTO) {

        Inventario savedInventario =
                inventarioService.save(
                        inventarioDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InventarioDTO.fromModel(savedInventario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario actualizado"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<InventarioDTO> update(
            @Parameter(description = "ID del inventario", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody InventarioDTO inventarioDTO) {

        if (inventarioService.findById(id) != null) {

            inventarioDTO.setId(id);

            Inventario updatedInventario =
                    inventarioService.save(
                            inventarioDTO.toModel());

            return ResponseEntity.ok(
                    InventarioDTO.fromModel(updatedInventario));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Inventario eliminado"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del inventario", example = "1", required = true)
            @PathVariable Long id) {

        if (inventarioService.findById(id) != null) {

            inventarioService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe un inventario")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existsInventario(
            @Parameter(description = "ID del inventario", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventarioService.existsById(id));
    }
}