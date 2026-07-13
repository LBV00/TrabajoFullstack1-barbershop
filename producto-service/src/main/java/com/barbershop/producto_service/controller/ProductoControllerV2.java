package com.barbershop.producto_service.controller;

import com.barbershop.producto_service.dto.ProductoDTO;
import com.barbershop.producto_service.model.Producto;
import com.barbershop.producto_service.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.barbershop.producto_service.assembler.ProductoModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/productos/v2")
@Tag(name = "Productos", description = "Operaciones para administrar productos")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    public ProductoControllerV2(
        ProductoService productoService,
        ProductoModelAssembler assembler) {

    this.productoService = productoService;
    this.assembler = assembler;
  }

    @GetMapping
    @Operation(summary = "(V2) Listar productos")
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> getAll() {

    List<EntityModel<ProductoDTO>> productos =
            productoService.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();

     CollectionModel<EntityModel<ProductoDTO>> collection =
            CollectionModel.of(
                    productos,
                    linkTo(methodOn(ProductoController.class)
                            .getAll())
                            .withSelfRel()
            );

     return ResponseEntity.ok(collection);
    }

@PostMapping
    public ResponseEntity<ProductoDTO> create(
            @Valid @RequestBody ProductoDTO productoDTO) {

        Producto nuevo =
                productoService.save(productoDTO.toModel());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductoDTO.fromModel(nuevo));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe un producto")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existsProducto(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoService.existsById(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "(V2) Buscar producto por ID")
public ResponseEntity<EntityModel<ProductoDTO>> obtenerPorId(
        @PathVariable Long id) {

    Producto producto = productoService.buscarPorId(id);

    if (producto == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(
            assembler.toModel(producto)
    );
}


    @Operation(summary = "Actualizar producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoDTO> actualizar(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDto) {

        Producto productoActual =
                productoService.buscarPorId(id);

        if (productoActual != null) {

            productoActual.setNombre(productoDto.getNombre());
            productoActual.setPrecio(productoDto.getPrecio());
            productoActual.setStock(productoDto.getStock());

            Producto productoActualizado =
                    productoService.save(productoActual);

            return ResponseEntity.ok(
                    ProductoDTO.fromModel(productoActualizado));
        }

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Eliminar producto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id) {

        Producto productoActual =
                productoService.buscarPorId(id);

        if (productoActual != null) {

            productoService.eliminarPorId(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
