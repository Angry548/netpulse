package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoriaIncidenciaRepository extends JpaRepository<CategoriaIncidencia, Integer> {

    Optional<CategoriaIncidencia> findByNombre(String nombre);

    Page<CategoriaIncidencia> findByNombreContainingIgnoreCase(
            String nombre,
            Pageable pageable
    );

    Page<CategoriaIncidencia> findAll(Pageable pageable);
}