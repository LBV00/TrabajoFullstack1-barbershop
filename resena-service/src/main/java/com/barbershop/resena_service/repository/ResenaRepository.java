package com.barbershop.resena_service.repository;

import com.barbershop.resena_service.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Endpoint personalizado: Buscar reseñas por cliente
    List<Resena> findByIdCliente(Long idCliente);
       // Búsqueda por atributo distinto al ID de la tabla
    
}
