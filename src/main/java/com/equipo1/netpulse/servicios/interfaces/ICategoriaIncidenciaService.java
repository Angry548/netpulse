package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoriaIncidenciaService {

    CategoriaIncidencia crear(CategoriaIncidencia categoria);

    CategoriaIncidencia buscarPorId(Integer id);

    CategoriaIncidencia buscarPorNombre(String nombre);

    Page<CategoriaIncidencia> buscarPorNombrePaginado(
            String nombre,
            Pageable pageable
    );

    List<CategoriaIncidencia> obtenerTodos();

    Page<CategoriaIncidencia> buscarTodosPaginados(
            Pageable pageable
    );

    CategoriaIncidencia actualizar(CategoriaIncidencia categoria);

    CategoriaIncidencia activar(CategoriaIncidencia categoria);

    CategoriaIncidencia desactivar(CategoriaIncidencia categoria);

    void eliminarPorId(Integer id);
}