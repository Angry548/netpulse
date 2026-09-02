package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Empleado;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmpleadoService {

    Empleado registrar(Empleado empleado);

    Empleado buscarPorId(Integer id);

    Empleado buscarPorCodigoEmpleado(String codigoEmpleado);

    List<Empleado> obtenerTodos();

    Page<Empleado> buscarTodosPaginados(Pageable pageable);

    Page<Empleado> buscarPorIdPaginado(
            Integer id,
            Pageable pageable
    );

    Page<Empleado> buscarPorCodigoEmpleado(
            String codigoEmpleado,
            Pageable pageable
    );

    Page<Empleado> buscarPorDepartamento(
            String departamento,
            Pageable pageable
    );

    Empleado obtenerPorUsuario(Usuario usuario);

    Empleado actualizar(Empleado empleado);

    Empleado activar(Empleado empleado);

    Empleado desactivar(Empleado empleado);

    void eliminarPorId(Integer id);
}