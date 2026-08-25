package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Mantenimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMantenimientoService {

    List<Mantenimiento> obtenerTodos();

    Page<Mantenimiento> buscarTodosPaginados(Pageable pageable);

    Mantenimiento buscarPorId(Integer id);

    Mantenimiento crearOEditar(Mantenimiento mantenimiento);

    void eliminarPorId(Integer id);
}
