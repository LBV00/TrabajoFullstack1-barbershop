package com.barbershop.reserva_service.controller;

import com.barbershop.reserva_service.model.Reserva;
import com.barbershop.reserva_service.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> getAll() {
        List<Reserva> reservas = reservaService.findAll();
        return reservas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getById(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.findById(id);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.save(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Reserva reserva) {
        try {
            if (reservaService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
            reserva.setId(id);
            return ResponseEntity.ok(reservaService.save(reserva));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (reservaService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        reservaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // Endpoint Personalizado 1: Buscar reservas de un cliente específico
    @GetMapping("/cliente/{idUsuario}")
    public ResponseEntity<List<Reserva>> getByIdUsuario(@PathVariable Long idUsuario) {
        List<Reserva> reservas = reservaService.findByIdUsuario(idUsuario);
        return reservas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reservas);
    }

    // Endpoint Personalizado 2: Obtener total de reservas históricas
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalReservas() {
        return ResponseEntity.ok(reservaService.contarTotalReservas());
    }
}