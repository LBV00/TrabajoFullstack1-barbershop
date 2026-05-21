package com.barbershop.resena_service.controller;

import com.barbershop.resena_service.dto.ResenaDTO;
import com.barbershop.resena_service.model.Resena;
import com.barbershop.resena_service.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    // GET todos
    @GetMapping
    public ResponseEntity<List<Resena>> getAll() {
        List<Resena> resenas = resenaService.findAll();
        return resenas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resenas);
    }

    // GET por ID
    @GetMapping("/{id}")
    public ResponseEntity<Resena> getById(@PathVariable Long id) {
        Optional<Resena> resena = resenaService.findById(id);
        return resena.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET por usuario (búsqueda por atributo distinto al ID de la tabla)
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Resena>> getByIdUsuario(@PathVariable Long idUsuario) {
        List<Resena> resenas = resenaService.findByIdUsuario(idUsuario);
        return resenas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resenas);
    }

    // GET total de reseñas
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalResenas() {
        return ResponseEntity.ok(resenaService.contarTotalResenas());
    }

    // POST: crear nueva reseña (valida con WebClient al user-service)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ResenaDTO resenaDto) {
        try {
            Resena nueva = resenaService.saveFromDto(resenaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: actualizar una reseña existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ResenaDTO resenaDto) {
        Optional<Resena> existente = resenaService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Resena actualizada = resenaService.actualizar(id, resenaDto);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (resenaService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        resenaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}