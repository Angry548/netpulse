package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {
}