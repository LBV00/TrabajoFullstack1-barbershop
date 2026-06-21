package com.barbershop.inventario_service.controller;

import com.barbershop.inventario_service.dto.InventarioDTO;
import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.service.InventarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventarios")
public class InventarioController {

    private static final Logger logger =
            LoggerFactory.getLogger(InventarioController.class);

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
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
    public ResponseEntity<InventarioDTO> getById(
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
    public ResponseEntity<InventarioDTO> create(
            @RequestBody InventarioDTO inventarioDTO) {

        Inventario savedInventario =
                inventarioService.save(
                        inventarioDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InventarioDTO.fromModel(savedInventario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> update(
            @PathVariable Long id,
            @RequestBody InventarioDTO inventarioDTO) {

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
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (inventarioService.findById(id) != null) {

            inventarioService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsInventario(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventarioService.existsById(id));
    }
}