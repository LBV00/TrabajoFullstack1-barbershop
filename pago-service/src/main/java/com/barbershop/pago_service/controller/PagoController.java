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

    @PostMapping
    public ResponseEntity<?> registrarPago(@Valid @RequestBody PagoDTO pagoDto) {
        try {
            Pago nuevoPago = pagoService.guardar(pagoDto.toModel());
            return ResponseEntity.status(HttpStatus.CREATED).body(PagoDTO.fromModel(nuevoPago));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        List<Pago> pagos = pagoService.listar();
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PagoDTO> dtos = pagos.stream().map(PagoDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> buscarPago(@PathVariable Long id) {
        return pagoService.buscarPorId(id)
                .map(pago -> ResponseEntity.ok(PagoDTO.fromModel(pago)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPago(@PathVariable Long id, @Valid @RequestBody PagoDTO pagoDto) {
        return pagoService.buscarPorId(id).map(pagoActual -> {
            try {
                pagoActual.setIdUsuario(pagoDto.getIdUsuario());
                pagoActual.setIdReserva(pagoDto.getIdReserva());
                pagoActual.setMonto(pagoDto.getMonto());
                pagoActual.setMetodoPago(pagoDto.getMetodoPago());

                Pago pagoActualizado = pagoService.guardar(pagoActual);
                return ResponseEntity.ok(PagoDTO.fromModel(pagoActualizado)); // Retorna 200 OK
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElse(ResponseEntity.notFound().build()); // 404 si el ID del pago no existe
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        return pagoService.buscarPorId(id).map(pago -> {
            pagoService.eliminarPorId(id);
            return ResponseEntity.noContent().<Void>build(); // Retorna 204 No Content según rúbrica
        }).orElse(ResponseEntity.notFound().build()); // 404 si intentan borrar algo que no existe
    }
     @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Pago>> buscarPagosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pagoService.findByIdUsuario(idUsuario));
    }

    // URL en Postman: GET http://localhost:7090/pagos/total
    @GetMapping("/total")
    public ResponseEntity<Long> totalDePagos() {
        return ResponseEntity.ok(pagoService.contarTotalPagos());
    }

}