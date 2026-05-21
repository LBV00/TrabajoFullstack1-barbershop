package com.barbershop.resena_service.controller;

import com.barbershop.resena_service.model.Resena;
import com.barbershop.resena_service.service.ResenaService;
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

    // CORRECCIÓN: Actualizado a idUsuario y findByIdUsuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Resena>> getByIdUsuario(@PathVariable Long idUsuario) {
        List<Resena> resenas = resenaService.findByIdUsuario(idUsuario);
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
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Resena resena) {
        Optional<Resena> existing = resenaService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Resena resenaActual = existing.get();
            resenaActual.setIdUsuario(resena.getIdUsuario());
            resenaActual.setIdReserva(resena.getIdReserva());
            resenaActual.setCalificacion(resena.getCalificacion());
            resenaActual.setComentario(resena.getComentario());
            Resena actualizada = resenaService.save(resenaActual);
            return ResponseEntity.ok(actualizada);
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