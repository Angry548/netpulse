package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Equipo;

import java.util.List;

public interface IEquipoService {

    Equipo registrar(Equipo equipo);

    Equipo buscarPorId(Integer id);

    Equipo buscarPorNumeroSerie(String numeroSerie);

    List<Equipo> obtenerTodos();

    Equipo actualizar(Equipo equipo);

    Equipo asignarResponsable(Equipo equipo);

    Equipo cambiarEstado(Equipo equipo);

    Equipo actualizarConexion(Equipo equipo);

    void eliminarPorId(Integer id);
}