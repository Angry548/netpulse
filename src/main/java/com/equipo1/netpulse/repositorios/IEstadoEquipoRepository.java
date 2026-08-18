package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoEquipoRepository extends JpaRepository<EstadoEquipo, Integer> {
}