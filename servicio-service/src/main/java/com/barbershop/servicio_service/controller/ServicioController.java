package com.barbershop.servicio_service.controller;

import com.barbershop.servicio_service.dto.ServicioDTO;
import com.barbershop.servicio_service.model.Servicio;
import com.barbershop.servicio_service.service.ServicioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    private static final Logger logger =
            LoggerFactory.getLogger(ServicioController.class);

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> getAll() {

        logger.info("Obteniendo servicios");

        List<Servicio> servicios = servicioService.findAll();

        if (servicios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ServicioDTO> dtos = servicios.stream()
                .map(ServicioDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> getById(@PathVariable Long id) {

        Servicio servicio = servicioService.findById(id);

        if (servicio != null) {
            return ResponseEntity.ok(
                    ServicioDTO.fromModel(servicio));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ServicioDTO> create(
            @RequestBody ServicioDTO servicioDTO) {

        Servicio savedServicio =
                servicioService.save(
                        servicioDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ServicioDTO.fromModel(savedServicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> update(
            @PathVariable Long id,
            @RequestBody ServicioDTO servicioDTO) {

        if (servicioService.findById(id) != null) {

            servicioDTO.setId(id);

            Servicio updatedServicio =
                    servicioService.save(
                            servicioDTO.toModel());

            return ResponseEntity.ok(
                    ServicioDTO.fromModel(updatedServicio));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (servicioService.findById(id) != null) {

            servicioService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsServicio(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                servicioService.existsById(id));
    }
}