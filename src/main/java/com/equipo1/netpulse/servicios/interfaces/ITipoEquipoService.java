package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.TipoEquipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITipoEquipoService {

    TipoEquipo crear(TipoEquipo tipoEquipo);

    TipoEquipo buscarPorId(Integer id);

    TipoEquipo buscarPorNombre(String nombre);

    List<TipoEquipo> obtenerTodos();

    Page<TipoEquipo> buscarTodosPaginados(Pageable pageable);

    TipoEquipo actualizar(TipoEquipo tipoEquipo);

    void eliminarPorId(Integer id);
}