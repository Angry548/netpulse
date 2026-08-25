package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.TipoEquipo;

import java.util.List;

public interface ITipoEquipoService {

    TipoEquipo crear(TipoEquipo tipoEquipo);

    TipoEquipo buscarPorId(Integer id);

    TipoEquipo buscarPorNombre(String nombre);

    List<TipoEquipo> obtenerTodos();

    TipoEquipo actualizar(TipoEquipo tipoEquipo);

    void eliminarPorId(Integer id);
}