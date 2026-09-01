package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Empleado;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {

    Empleado save(Empleado empleado);

    Optional<Empleado> findById(Integer id);

    Optional<Empleado> findByCodigoEmpleado(String codigoEmpleado);

    List<Empleado> findAll();

    Page<Empleado> findAll(Pageable pageable);

    Optional<Empleado> findByUsuario(Usuario usuario);

    void deleteById(Integer id);
}