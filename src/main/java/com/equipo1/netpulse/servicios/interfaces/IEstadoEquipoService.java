package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEstadoEquipoService {

    EstadoEquipo crear(EstadoEquipo estadoEquipo);

    EstadoEquipo buscarPorId(Integer id);

    EstadoEquipo buscarPorNombre(String nombre);

    List<EstadoEquipo> obtenerTodos();

    Page<EstadoEquipo> buscarTodosPaginados(Pageable pageable);

    Page<EstadoEquipo> buscarPorIdPaginado(
            Integer id,
            Pageable pageable
    );

    Page<EstadoEquipo> buscarPorNombre(
            String nombre,
            Pageable pageable
    );

    EstadoEquipo actualizar(EstadoEquipo estadoEquipo);

    void eliminarPorId(Integer id);
}