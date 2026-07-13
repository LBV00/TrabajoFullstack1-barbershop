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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/sucursales")
@Tag(name = "Sucursales", description = "Operaciones para administrar sucursales")
public class SucursalController {

    private static final Logger logger =
            LoggerFactory.getLogger(SucursalController.class);

    private final SucursalService sucursalService;
    
    public SucursalController(
        SucursalService sucursalService) {

    this.sucursalService = sucursalService;
    
   }
    @GetMapping
    @Operation(summary = "(V1) Listar sucursales")
      public ResponseEntity<List<SucursalDTO>> getAll() {
        List<SucursalDTO> lista = sucursalService.findAll()
                .stream()
                .map(SucursalDTO::fromModel)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "(V1) Buscar sucursal por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
     })
        public ResponseEntity<SucursalDTO> getById(@PathVariable Long id) {
        Sucursal model = sucursalService.findById(id);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(SucursalDTO.fromModel(model));
    }

    @PostMapping
    @Operation(summary = "Crear sucursal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sucursal creada",
                    content = @Content(schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<SucursalDTO> create(@Valid @RequestBody SucursalDTO sucursalDTO) {

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
            @Valid @RequestBody SucursalDTO sucursalDTO) {

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