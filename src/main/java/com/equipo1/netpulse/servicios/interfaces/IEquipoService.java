package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEquipoService {

    Page<Equipo> obtenerTodosPaginados(Pageable pageable);

    List<Equipo> obtenerTodos();

    Equipo obtenerPorId(Integer id);

    Equipo obtenerPorNumeroSerie(String numeroSerie);

    Equipo crearOEditar(Equipo equipo);

    void eliminarPorId(Integer id);
}