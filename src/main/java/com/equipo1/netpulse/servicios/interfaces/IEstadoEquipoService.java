package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEstadoEquipoService {

    Page<EstadoEquipo> obtenerTodosPaginados(Pageable pageable);

    List<EstadoEquipo> obtenerTodos();

    EstadoEquipo obtenerPorId(Integer id);

    EstadoEquipo crearOEditar(EstadoEquipo estadoEquipo);

    void eliminarPorId(Integer id);

    EstadoEquipo obtenerPorNombre(String nombre);
}