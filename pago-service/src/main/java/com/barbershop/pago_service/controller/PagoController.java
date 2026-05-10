package com.barbershop.pago_service.controller;

import com.barbershop.pago_service.model.Pago;
import com.barbershop.pago_service.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> getAll() {
        List<Pago> pagos = pagoService.findAll();
        return pagos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getById(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.findById(id);
        return pago.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> create(@RequestBody Pago pago) {
        Pago nuevoPago = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> update(@PathVariable Long id, @RequestBody Pago pago) {
        if (pagoService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        pago.setId(id);
        return ResponseEntity.ok(pagoService.save(pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (pagoService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        pagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}