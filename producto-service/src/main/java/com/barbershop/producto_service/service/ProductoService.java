package com.barbershop.producto_service.service;

import com.barbershop.producto_service.model.Producto;
import com.barbershop.producto_service.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        log.info("Listando todos los productos.");
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        log.info("Guardando producto: {}", producto.getNombre());
        return productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        log.warn("Eliminando producto con ID: {}", id);
        productoRepository.deleteById(id);
    }

    public Boolean existsById(Long id) {
        return productoRepository.existsById(id);
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public void eliminarPorId(Long id) {
        log.warn("Eliminando producto con ID: {}", id);
        productoRepository.deleteById(id);
    }
}