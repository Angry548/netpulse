package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.EstadoEquipo;

import java.util.List;

public interface IEstadoEquipoService {

    EstadoEquipo crear(EstadoEquipo estadoEquipo);

    EstadoEquipo buscarPorId(Integer id);

    EstadoEquipo buscarPorNombre(String nombre);

    List<EstadoEquipo> obtenerTodos();

    EstadoEquipo actualizar(EstadoEquipo estadoEquipo);

    void eliminarPorId(Integer id);
}