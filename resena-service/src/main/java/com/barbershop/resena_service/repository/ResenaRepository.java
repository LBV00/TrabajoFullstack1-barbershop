package com.barbershop.resena_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.barbershop.resena_service.model.Resena;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    
    List<Resena> findByIdUsuario(Long idUsuario);
}