package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolService {

    Page<Rol> obtenerTodosPaginados(Pageable pageable);

    List<Rol> obtenerTodos();

    Rol obtenerPorId(Integer id);

    Rol crearOEditar(Rol rol);

    void eliminarPorId(Integer id);

    Rol obtenerPorNombre(String nombre);
}