package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Rol;

import java.util.List;

public interface IRolService {

    Rol crear(Rol rol);

    Rol buscarPorId(Integer id);

    Rol buscarPorNombre(String nombre);

    List<Rol> obtenerTodos();

    Rol actualizar(Rol rol);

    void eliminarPorId(Integer id);

    void asignarUsuario(Rol rol);
}