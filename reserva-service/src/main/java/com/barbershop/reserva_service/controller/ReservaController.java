package com.barbershop.reserva_service.controller;

import com.barbershop.reserva_service.dto.ReservaDTO;
import com.barbershop.reserva_service.model.Reserva;
import com.barbershop.reserva_service.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> getAll() {
        List<Reserva> reservas = reservaService.findAll();
        if (reservas.isEmpty()) return ResponseEntity.noContent().build();
        List<ReservaDTO> dtos = reservas.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getById(@PathVariable Long id) {
        return reservaService.findById(id)
                .map(r -> ResponseEntity.ok(toDTO(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{idUsuario}")
    public ResponseEntity<List<ReservaDTO>> getByIdUsuario(@PathVariable Long idUsuario) {
        List<Reserva> reservas = reservaService.findByIdUsuario(idUsuario);
        if (reservas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(reservas.stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalReservas() {
        return ResponseEntity.ok(reservaService.contarTotalReservas());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReservaDTO dto) {
        try {
            Reserva nueva = reservaService.save(toEntity(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(nueva));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ReservaDTO dto) {
        if (reservaService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        try {
            Reserva entidad = toEntity(dto);
            entidad.setId(id);
            return ResponseEntity.ok(toDTO(reservaService.save(entidad)));
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

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.existePorId(id));
    }

    // Convierte entidad → DTO
    private ReservaDTO toDTO(Reserva r) {
        return ReservaDTO.builder()
                .id(r.getId())
                .idUsuario(r.getIdUsuario())
                .fechaReserva(r.getFechaReserva())
                .total(r.getTotal())
                .estado(r.getEstado())
                .build();
    }

    // Convierte DTO → entidad
    private Reserva toEntity(ReservaDTO dto) {
        return Reserva.builder()
                .idUsuario(dto.getIdUsuario())
                .fechaReserva(dto.getFechaReserva())
                .total(dto.getTotal())
                .estado(dto.getEstado())
                .build();
    }
}