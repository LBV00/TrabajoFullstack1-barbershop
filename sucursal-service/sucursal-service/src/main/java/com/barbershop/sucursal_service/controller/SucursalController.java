package com.barbershop.sucursal_service.controller;

import com.barbershop.sucursal_service.dto.SucursalDTO;
import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.service.SucursalService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sucursales")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> getAll() {

        List<SucursalDTO> sucursales =
                service.findAll()
                        .stream()
                        .map(SucursalDTO::fromModel)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> getById(
            @PathVariable Long id) {

        Sucursal sucursal = service.findById(id);

        if(sucursal == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                SucursalDTO.fromModel(sucursal)
        );
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> create(
            @Valid @RequestBody SucursalDTO dto) {

        Sucursal saved =
                service.save(dto.toModel());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SucursalDTO.fromModel(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SucursalDTO dto) {

        dto.setId(id);

        Sucursal updated =
                service.save(dto.toModel());

        return ResponseEntity.ok(
                SucursalDTO.fromModel(updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.existsById(id)
        );
    }
}