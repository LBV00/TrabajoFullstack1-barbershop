package com.barbershop.pago_service.controller;

import com.barbershop.pago_service.dto.PagoDTO;
import com.barbershop.pago_service.model.Pago;
import com.barbershop.pago_service.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // POST: Recibe un PagoDTO, lo valida con @Valid y dispara la doble comunicación
    @PostMapping
    public ResponseEntity<?> registrarPago(@Valid @RequestBody PagoDTO pagoDto) {
        try {
            Pago nuevoPago = pagoService.guardar(pagoDto.toModel());
            return ResponseEntity.status(HttpStatus.CREATED).body(PagoDTO.fromModel(nuevoPago));
        } catch (RuntimeException e) {
            // Si el webclient arroja un false, capturamos el error y devolvemos 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: Lista todos los pagos en formato DTO
    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        List<Pago> pagos = pagoService.listar();
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PagoDTO> dtos = pagos.stream().map(PagoDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET: Buscar un pago específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> buscarPago(@PathVariable Long id) {
        return pagoService.buscarPorId(id)
                .map(pago -> ResponseEntity.ok(PagoDTO.fromModel(pago)))
                .orElse(ResponseEntity.notFound().build());
    }
}