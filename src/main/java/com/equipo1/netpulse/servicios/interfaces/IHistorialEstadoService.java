package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.HistorialEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHistorialEstadoService {

    List<HistorialEstado> obtenerTodos();

    Page<HistorialEstado> buscarTodosPaginados(Pageable pageable);

    HistorialEstado buscarPorId(Integer id);

    HistorialEstado crearOEditar(HistorialEstado historialEstado);

    void eliminarPorId(Integer id);
}