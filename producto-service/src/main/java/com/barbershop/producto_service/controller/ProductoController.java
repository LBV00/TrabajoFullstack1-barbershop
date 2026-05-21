package com.barbershop.producto_service.controller;

import com.barbershop.producto_service.dto.ProductoDTO;
import com.barbershop.producto_service.model.Producto;
import com.barbershop.producto_service.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAll() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        
        List<ProductoDTO> dtos = productos.stream().map(ProductoDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> create(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto nuevo = productoService.save(productoDTO.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoDTO.fromModel(nuevo));
    }

    // Endpoint crucial consumido remotamente por reserva-service
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.existsById(id));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(ProductoDTO.fromModel(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}