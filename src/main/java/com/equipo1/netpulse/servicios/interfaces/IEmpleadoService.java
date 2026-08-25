package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Empleado;

import java.util.List;

public interface IEmpleadoService {

    Empleado registrar(Empleado empleado);

    Empleado buscarPorId(Integer id);

    Empleado buscarPorCodigoEmpleado(String codigoEmpleado);

    List<Empleado> obtenerTodos();

    Empleado actualizar(Empleado empleado);

    Empleado activar(Empleado empleado);

    Empleado desactivar(Empleado empleado);

    void eliminarPorId(Integer id);
}