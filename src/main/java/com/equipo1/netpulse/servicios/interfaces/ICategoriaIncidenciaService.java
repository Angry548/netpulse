package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;

import java.util.List;

public interface ICategoriaIncidenciaService {

    CategoriaIncidencia crear(CategoriaIncidencia categoria);

    CategoriaIncidencia buscarPorId(Integer id);

    CategoriaIncidencia buscarPorNombre(String nombre);

    List<CategoriaIncidencia> obtenerTodos();

    CategoriaIncidencia actualizar(CategoriaIncidencia categoria);

    CategoriaIncidencia activar(CategoriaIncidencia categoria);

    CategoriaIncidencia desactivar(CategoriaIncidencia categoria);

    void eliminarPorId(Integer id);
}
