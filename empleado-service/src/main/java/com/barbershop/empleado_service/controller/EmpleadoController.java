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

    private final EmpleadoService empleadoService;
    
    public EmpleadoController(
        EmpleadoService empleadoService) {

    this.empleadoService = empleadoService;
    
   }

    @GetMapping
    @Operation(summary = "(V1) Listar empleados")
         public ResponseEntity<List<EmpleadoDTO>> getAll() {
        List<EmpleadoDTO> lista = empleadoService.findAll()
                .stream()
                .map(EmpleadoDTO::fromModel)
                .toList();
        return ResponseEntity.ok(lista);
    }  

    @GetMapping("/{id}")
    @Operation(summary = "(V1) Buscar empleado por ID")
         public ResponseEntity<EmpleadoDTO> getById(@PathVariable Long id) {
        Empleado model = empleadoService.findById(id);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(EmpleadoDTO.fromModel(model));
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