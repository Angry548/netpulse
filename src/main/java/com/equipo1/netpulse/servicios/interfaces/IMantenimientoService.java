package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.modelos.Ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMantenimientoService {

    Mantenimiento registrar(Mantenimiento mantenimiento);

    Mantenimiento buscarPorId(Integer id);

    List<Mantenimiento> obtenerTodos();

    Page<Mantenimiento> buscarTodosPaginados(Pageable pageable);

    List<Mantenimiento> obtenerPorEquipo(Equipo equipo);

    List<Mantenimiento> obtenerPorUsuario(Usuario usuario);

    List<Mantenimiento> obtenerPorTicket(Ticket ticket);

    Mantenimiento actualizar(Mantenimiento mantenimiento);

    void eliminarPorId(Integer id);
}
