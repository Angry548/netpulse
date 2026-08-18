package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.TipoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoEquipoRepository extends JpaRepository<TipoEquipo, Integer> {
}