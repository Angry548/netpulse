package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Mantenimiento;

import java.util.List;

public interface IMantenimientoService {

    Mantenimiento registrar(Mantenimiento mantenimiento);

    Mantenimiento buscarPorId(Integer id);

    List<Mantenimiento> obtenerTodos();

    Mantenimiento actualizar(Mantenimiento mantenimiento);

    Mantenimiento finalizar(Mantenimiento mantenimiento);

    void eliminarPorId(Integer id);
}
