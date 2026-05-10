package com.barbershop.resena_service.controller;

import com.barbershop.resena_service.model.Resena;
import com.barbershop.resena_service.service.ResenaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public ResponseEntity<List<Resena>> getAll() {
        List<Resena> resenas = resenaService.findAll();
        return resenas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resenas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> getById(@PathVariable Long id) {
        Optional<Resena> resena = resenaService.findById(id);
        return resena.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint Personalizado
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Resena>> getByIdCliente(@PathVariable Long idCliente) {
        List<Resena> resenas = resenaService.findByIdCliente(idCliente);
        return resenas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resenas);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Resena resena) {
        try {
            Resena nuevaResena = resenaService.save(resena);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaResena);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (resenaService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        resenaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalResenas() {
        return ResponseEntity.ok(resenaService.contarTotalResenas());
    }

}