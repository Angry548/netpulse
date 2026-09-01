package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolService {

    Rol crear(Rol rol);

    Rol buscarPorId(Integer id);

    Rol buscarPorNombre(String nombre);

    List<Rol> obtenerTodos();

    Page<Rol> buscarTodosPaginados(Pageable pageable);

    Rol actualizar(Rol rol);

    void eliminarPorId(Integer id);
}