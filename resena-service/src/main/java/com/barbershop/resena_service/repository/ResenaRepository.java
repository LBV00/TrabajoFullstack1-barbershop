package com.barbershop.resena_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.barbershop.resena_service.model.Resena;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    
    // Spring Data JPA crea la query automática basada en este nombre
    List<Resena> findByIdUsuario(Long idUsuario);
}