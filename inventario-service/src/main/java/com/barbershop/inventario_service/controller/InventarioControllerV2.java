package com.barbershop.inventario_service.controller;

import com.barbershop.inventario_service.dto.InventarioDTO;
import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.barbershop.inventario_service.assembler.InventarioModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/inventarios/v2")
@Tag(name = "Inventario", description = "Operaciones para administrar inventario")
public class InventarioControllerV2 {

    private static final Logger logger =
            LoggerFactory.getLogger(InventarioControllerV2.class);

    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler;
    public InventarioControllerV2(
        InventarioService inventarioService,
        InventarioModelAssembler assembler) {
     this.inventarioService = inventarioService;
     this.assembler = assembler;
     }

    @GetMapping
    @Operation(summary = "(V2) Listar inventario")
     public ResponseEntity<CollectionModel<EntityModel<InventarioDTO>>> getAll() {
    logger.info("Obteniendo inventario");
    List<EntityModel<InventarioDTO>> inventarios =
            inventarioService.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    CollectionModel<EntityModel<InventarioDTO>> collection =
            CollectionModel.of(
                    inventarios,
                    linkTo(methodOn(InventarioController.class)
                            .getAll())
                            .withSelfRel()
            );

    return ResponseEntity.ok(collection);
     }

    @GetMapping("/{id}")
    @Operation(summary = "(V2) Buscar inventario por ID")
    public ResponseEntity<EntityModel<InventarioDTO>> getById(
        @PathVariable Long id) {
    Inventario inventario =
            inventarioService.findById(id);
    if (inventario == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(
            assembler.toModel(inventario)
    );
    } 

@PostMapping
    public ResponseEntity<InventarioDTO> create(
            @Valid @RequestBody InventarioDTO inventarioDTO) {

        Inventario savedInventario =
                inventarioService.save(
                        inventarioDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InventarioDTO.fromModel(savedInventario));
    }


    @Operation(summary = "Actualizar inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario actualizado"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<InventarioDTO> update(
            @Parameter(description = "ID del inventario", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody InventarioDTO inventarioDTO) {

        if (inventarioService.findById(id) != null) {

            inventarioDTO.setId(id);

            Inventario updatedInventario =
                    inventarioService.save(
                            inventarioDTO.toModel());

            return ResponseEntity.ok(
                    InventarioDTO.fromModel(updatedInventario));
        }

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Eliminar inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Inventario eliminado"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del inventario", example = "1", required = true)
            @PathVariable Long id) {

        if (inventarioService.findById(id) != null) {

            inventarioService.delete(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe un inventario")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existsInventario(
            @Parameter(description = "ID del inventario", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventarioService.existsById(id));
    }
}
