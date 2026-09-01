package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.HistorialEstado;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHistorialEstadoService {

    HistorialEstado registrarCambio(HistorialEstado historial);

    HistorialEstado buscarPorId(Integer id);

    List<HistorialEstado> obtenerTodos();

    List<HistorialEstado> obtenerPorEquipo(Equipo equipo);

    List<HistorialEstado> obtenerPorUsuario(Usuario usuario);

    void eliminarPorId(Integer id);
}