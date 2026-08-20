package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.TipoEquipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITipoEquipoService {

    Page<TipoEquipo> obtenerTodosPaginados(Pageable pageable);

    List<TipoEquipo> obtenerTodos();

    TipoEquipo obtenerPorId(Integer id);

    TipoEquipo crearOEditar(TipoEquipo tipoEquipo);

    void eliminarPorId(Integer id);

    TipoEquipo obtenerPorNombre(String nombre);
}