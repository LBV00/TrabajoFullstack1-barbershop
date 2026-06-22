package com.barbershop.empleado_service.controller;

import com.barbershop.empleado_service.dto.EmpleadoDTO;
import com.barbershop.empleado_service.model.Empleado;
import com.barbershop.empleado_service.service.EmpleadoService;

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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/empleados")
@Tag(name = "Empleados", description = "Operaciones para administrar empleados")
public class EmpleadoController {

    private static final Logger logger =
            LoggerFactory.getLogger(EmpleadoController.class);

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    @Operation(summary = "Listar empleados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleados encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmpleadoDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No existen empleados", content = @Content)
    })
    public ResponseEntity<List<EmpleadoDTO>> getAll() {

        logger.info("Obteniendo empleados");

        List<Empleado> empleados = empleadoService.findAll();

        if (empleados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EmpleadoDTO> dtos = empleados.stream()
                .map(EmpleadoDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empleado por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado encontrado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public ResponseEntity<EmpleadoDTO> getById(
            @Parameter(description = "ID del empleado", example = "1", required = true)
            @PathVariable Long id) {

        Empleado empleado = empleadoService.findById(id);

        if (empleado != null) {
            return ResponseEntity.ok(
                    EmpleadoDTO.fromModel(empleado));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear empleado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empleado creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EmpleadoDTO> create(
            @Valid @RequestBody EmpleadoDTO empleadoDTO) {

        Empleado savedEmpleado =
                empleadoService.save(
                        empleadoDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmpleadoDTO.fromModel(savedEmpleado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empleado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado actualizado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public ResponseEntity<EmpleadoDTO> update(
            @Parameter(description = "ID del empleado", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoDTO empleadoDTO) {

        if (empleadoService.findById(id) != null) {

            empleadoDTO.setId(id);

            Empleado updatedEmpleado =
                    empleadoService.save(
                            empleadoDTO.toModel());

            return ResponseEntity.ok(
                    EmpleadoDTO.fromModel(updatedEmpleado));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empleado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Empleado eliminado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del empleado", example = "1", required = true)
            @PathVariable Long id) {

        if (empleadoService.findById(id) != null) {

            empleadoService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe un empleado")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existsEmpleado(
            @Parameter(description = "ID del empleado", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                empleadoService.existsById(id));
    }
}