package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEquipoService {

    Equipo registrar(Equipo equipo);

    Equipo buscarPorId(Integer id);

    Equipo buscarPorNumeroSerie(String numeroSerie);

    List<Equipo> obtenerTodos();

    Page<Equipo> buscarTodosPaginados(Pageable pageable);

    List<Equipo> obtenerPorTipo(TipoEquipo tipoEquipo);

    List<Equipo> obtenerPorEstado(EstadoEquipo estadoEquipo);

    List<Equipo> obtenerPorResponsable(Usuario usuario);

    Equipo actualizar(Equipo equipo);

    Equipo asignarResponsable(Equipo equipo);

    Equipo cambiarEstado(Equipo equipo);

    Equipo actualizarConexion(Equipo equipo);

    void eliminarPorId(Integer id);
}