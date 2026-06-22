package com.barbershop.reserva_service.controller;

import com.barbershop.reserva_service.dto.ReservaDTO;
import com.barbershop.reserva_service.model.Reserva;
import com.barbershop.reserva_service.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Operaciones para administrar reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Listar reservas", description = "Obtiene todas las reservas registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservaDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No existen reservas", content = @Content)
    })
    public ResponseEntity<List<ReservaDTO>> getAll() {
        List<Reserva> reservas = reservaService.findAll();

        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ReservaDTO> dtos = reservas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
    })
    public ResponseEntity<ReservaDTO> getById(
            @Parameter(description = "ID de la reserva", example = "1", required = true)
            @PathVariable Long id) {

        return reservaService.findById(id)
                .map(r -> ResponseEntity.ok(toDTO(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{idUsuario}")
    @Operation(summary = "Buscar reservas por usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
            @ApiResponse(responseCode = "204", description = "No existen reservas para el usuario")
    })
    public ResponseEntity<List<ReservaDTO>> getByIdUsuario(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long idUsuario) {

        List<Reserva> reservas = reservaService.findByIdUsuario(idUsuario);

        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                reservas.stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/total")
    @Operation(summary = "Obtener cantidad total de reservas")
    @ApiResponse(responseCode = "200", description = "Total de reservas")
    public ResponseEntity<Long> getTotalReservas() {
        return ResponseEntity.ok(reservaService.contarTotalReservas());
    }

    @PostMapping
    @Operation(summary = "Crear reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<?> create(@Valid @RequestBody ReservaDTO dto) {

        try {
            Reserva nueva = reservaService.save(toEntity(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(nueva));

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID de la reserva", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO dto) {

        if (reservaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {

            Reserva entidad = toEntity(dto);
            entidad.setId(id);

            return ResponseEntity.ok(
                    toDTO(reservaService.save(entidad)));

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la reserva", example = "1", required = true)
            @PathVariable Long id) {

        if (reservaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        reservaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe una reserva")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existeReserva(
            @Parameter(description = "ID de la reserva", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservaService.existePorId(id));
    }

    private ReservaDTO toDTO(Reserva r) {
        return ReservaDTO.builder()
                .id(r.getId())
                .idUsuario(r.getIdUsuario())
                .fechaReserva(r.getFechaReserva())
                .total(r.getTotal())
                .estado(r.getEstado())
                .build();
    }

    private Reserva toEntity(ReservaDTO dto) {
        return Reserva.builder()
                .idUsuario(dto.getIdUsuario())
                .fechaReserva(dto.getFechaReserva())
                .total(dto.getTotal())
                .estado(dto.getEstado())
                .build();
    }
}