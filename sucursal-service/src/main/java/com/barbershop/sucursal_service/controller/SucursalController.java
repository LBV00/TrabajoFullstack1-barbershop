package com.barbershop.sucursal_service.controller;

import com.barbershop.sucursal_service.dto.SucursalDTO;
import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.service.SucursalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sucursales")
public class SucursalController {

    private static final Logger logger =
            LoggerFactory.getLogger(SucursalController.class);

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
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
    public ResponseEntity<SucursalDTO> getById(
            @PathVariable Long id) {

        logger.info("Buscando sucursal {}", id);

        Sucursal sucursal = sucursalService.findById(id);

        if (sucursal != null) {
            return ResponseEntity.ok(
                    SucursalDTO.fromModel(sucursal));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> create(
            @RequestBody SucursalDTO sucursalDTO) {

        logger.info("Creando sucursal {}",
                sucursalDTO.getNombre());

        Sucursal savedSucursal =
                sucursalService.save(
                        sucursalDTO.toModel());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SucursalDTO.fromModel(savedSucursal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> update(
            @PathVariable Long id,
            @RequestBody SucursalDTO sucursalDTO) {

        if (sucursalService.findById(id) != null) {

            sucursalDTO.setId(id);

            Sucursal updatedSucursal =
                    sucursalService.save(
                            sucursalDTO.toModel());

            return ResponseEntity.ok(
                    SucursalDTO.fromModel(updatedSucursal));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        logger.info("Eliminando sucursal {}", id);

        if (sucursalService.findById(id) != null) {

            sucursalService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsSucursal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sucursalService.existsById(id));
    }
}