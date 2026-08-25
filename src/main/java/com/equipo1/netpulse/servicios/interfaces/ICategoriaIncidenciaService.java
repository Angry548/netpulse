package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoriaIncidenciaService {

    Page<CategoriaIncidencia> obtenerTodosPaginados(Pageable pageable);

    List<CategoriaIncidencia> obtenerTodos();

    CategoriaIncidencia obtenerPorId(Integer id);

    CategoriaIncidencia obtenerPorNombre(String nombre);

    CategoriaIncidencia crearOEditar(CategoriaIncidencia categoriaIncidencia);

    void eliminarPorId(Integer id);
}
