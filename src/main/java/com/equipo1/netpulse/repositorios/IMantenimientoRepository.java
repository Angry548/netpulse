package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {
}
