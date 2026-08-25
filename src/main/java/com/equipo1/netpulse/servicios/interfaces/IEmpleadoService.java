package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmpleadoService {

    Page<Empleado> obtenerTodosPaginados(Pageable pageable);

    List<Empleado> obtenerTodos();

    Empleado obtenerPorId(Integer id);

    Empleado crearOEditar(Empleado empleado);

    void eliminarPorId(Integer id);

    Empleado obtenerPorCodigoEmpleado(String codigoEmpleado);
}