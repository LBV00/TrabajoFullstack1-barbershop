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
        // PUT: Actualizar un Pago (re-valida con WebClient)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPago(@PathVariable Long id, @Valid @RequestBody PagoDTO pagoDto) {
        return pagoService.buscarPorId(id).map(pagoActual -> {
            try {
                // Actualizamos los datos con lo que viene del JSON
                pagoActual.setIdUsuario(pagoDto.getIdUsuario());
                pagoActual.setIdReserva(pagoDto.getIdReserva());
                pagoActual.setMonto(pagoDto.getMonto());
                pagoActual.setMetodoPago(pagoDto.getMetodoPago());

                // Guardamos (esto vuelve a disparar la validación del WebClient)
                Pago pagoActualizado = pagoService.guardar(pagoActual);
                return ResponseEntity.ok(PagoDTO.fromModel(pagoActualizado)); // Retorna 200 OK
            } catch (RuntimeException e) {
                // Si actualizan a un usuario/reserva falsa, lanza 400 Bad Request
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElse(ResponseEntity.notFound().build()); // 404 si el ID del pago no existe
    }

    // DELETE: Eliminar un Pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        return pagoService.buscarPorId(id).map(pago -> {
            pagoService.eliminarPorId(id);
            return ResponseEntity.noContent().<Void>build(); // Retorna 204 No Content según rúbrica
        }).orElse(ResponseEntity.notFound().build()); // 404 si intentan borrar algo que no existe
    }

}