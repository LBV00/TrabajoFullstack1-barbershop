package com.barbershop.empleado_service.controller;

import com.barbershop.empleado_service.dto.EmpleadoDTO;
import com.barbershop.empleado_service.model.Empleado;
import com.barbershop.empleado_service.service.EmpleadoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private static final Logger logger =
            LoggerFactory.getLogger(EmpleadoController.class);

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
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
    public ResponseEntity<EmpleadoDTO> getById(@PathVariable Long id) {

        Empleado empleado = empleadoService.findById(id);

        if (empleado != null) {
            return ResponseEntity.ok(
                    EmpleadoDTO.fromModel(empleado));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> create(
            @RequestBody EmpleadoDTO empleadoDTO) {

        Empleado savedEmpleado =
                empleadoService.save(
                        empleadoDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmpleadoDTO.fromModel(savedEmpleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> update(
            @PathVariable Long id,
            @RequestBody EmpleadoDTO empleadoDTO) {

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
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (empleadoService.findById(id) != null) {

            empleadoService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsEmpleado(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                empleadoService.existsById(id));
    }
}
