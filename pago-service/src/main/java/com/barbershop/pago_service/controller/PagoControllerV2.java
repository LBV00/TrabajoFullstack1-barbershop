package com.barbershop.pago_service.controller;

import com.barbershop.pago_service.dto.PagoDTO;
import com.barbershop.pago_service.model.Pago;
import com.barbershop.pago_service.service.PagoService;
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
import com.barbershop.pago_service.assembler.PagoModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.stream.Collectors;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pagos/v2")
@Tag(name = "Pagos", description = "Operaciones para administrar pagos")
public class PagoControllerV2 {

    private final PagoService pagoService;
    private final PagoModelAssembler assembler;

    public PagoControllerV2(
        PagoService pagoService,
        PagoModelAssembler assembler) {

    this.pagoService = pagoService;
    this.assembler = assembler;
   }

    @PostMapping
    @Operation(summary = "(V2) Registrar pago")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<?> registrarPago(@Valid @RequestBody PagoDTO pagoDto) {
        try {
            Pago nuevoPago = pagoService.guardar(pagoDto.toModel());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PagoDTO.fromModel(nuevoPago));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "(V2) Listar pagos")
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> listarPagos() {

    List<EntityModel<PagoDTO>> pagos =
            pagoService.listar()
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    CollectionModel<EntityModel<PagoDTO>> collection =
            CollectionModel.of(
                    pagos,
                    linkTo(methodOn(PagoController.class)
                            .listarPagos())
                            .withSelfRel()
            );

    return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "(V2) Buscar pago por ID")
    public ResponseEntity<EntityModel<PagoDTO>> buscarPago(
        @PathVariable Long id) {

    return pagoService.buscarPorId(id)
            .map(assembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
 }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago actualizado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<?> actualizarPago(
            @Parameter(description = "ID del pago", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody PagoDTO pagoDto) {

        return pagoService.buscarPorId(id).map(pagoActual -> {
            try {

                pagoActual.setIdUsuario(pagoDto.getIdUsuario());
                pagoActual.setIdReserva(pagoDto.getIdReserva());
                pagoActual.setMonto(pagoDto.getMonto());
                pagoActual.setMetodoPago(pagoDto.getMetodoPago());

                Pago pagoActualizado = pagoService.guardar(pagoActual);

                return ResponseEntity.ok(
                        PagoDTO.fromModel(pagoActualizado));

            } catch (RuntimeException e) {

                return ResponseEntity.badRequest()
                        .body(e.getMessage());
            }

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID del pago", example = "1", required = true)
            @PathVariable Long id) {

        return pagoService.buscarPorId(id).map(pago -> {

            pagoService.eliminarPorId(id);

            return ResponseEntity.noContent().<Void>build();

        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Buscar pagos por usuario")
    public ResponseEntity<List<Pago>> buscarPagosPorUsuario(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                pagoService.findByIdUsuario(idUsuario));
    }

    @GetMapping("/total")
    @Operation(summary = "Obtener cantidad total de pagos")
    public ResponseEntity<Long> totalDePagos() {

        return ResponseEntity.ok(
                pagoService.contarTotalPagos());
    }
}
