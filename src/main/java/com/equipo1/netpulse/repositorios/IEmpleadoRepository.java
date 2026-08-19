package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {

    Optional<Empleado> findByCodigoEmpleado(String codigoEmpleado);
}
