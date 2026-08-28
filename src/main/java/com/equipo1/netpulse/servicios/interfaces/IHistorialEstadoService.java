package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.HistorialEstado;

import java.util.List;

public interface IHistorialEstadoService {

    HistorialEstado registrarCambio(HistorialEstado historial);

    HistorialEstado buscarPorId(Integer id);

    List<HistorialEstado> obtenerTodos();

    void eliminarPorId(Integer id);
}