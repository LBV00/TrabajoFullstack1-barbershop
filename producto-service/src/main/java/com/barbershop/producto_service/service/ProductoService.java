package com.barbershop.producto_service.service;

import com.barbershop.producto_service.model.Producto;
import com.barbershop.producto_service.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() { return productoRepository.findAll(); }

    public Optional<Producto> findById(Long id) { return productoRepository.findById(id); }

    public Producto save(Producto producto) { return productoRepository.save(producto); }

    public void deleteById(Long id) { productoRepository.deleteById(id); }

    // Vital para la comunicación con reserva-service
    public Boolean existsById(Long id) { return productoRepository.existsById(id); }


    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
    
    public void eliminarPorId(Long id) {
        productoRepository.deleteById(id);
    }
}
