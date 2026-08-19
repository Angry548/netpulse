package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEstadoEquipoRepository extends JpaRepository<EstadoEquipo, Integer> {

    Optional<EstadoEquipo> findByNombre(String nombre);
}
