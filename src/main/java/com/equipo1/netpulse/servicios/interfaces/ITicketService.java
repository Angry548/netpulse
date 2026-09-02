package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.modelos.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITicketService {

    Ticket registrar(Ticket ticket);

    Ticket buscarPorId(Integer id);

    List<Ticket> obtenerTodos();

    Page<Ticket> buscarTodosPaginados(Pageable pageable);

    List<Ticket> obtenerPorUsuarioReporta(Usuario usuario);

    List<Ticket> obtenerPorTecnico(Usuario usuario);

    List<Ticket> obtenerPorEquipo(Equipo equipo);

    List<Ticket> obtenerPorCategoria(CategoriaIncidencia categoria);

    List<Ticket> obtenerPorPrioridad(PrioridadTicket prioridad);

    List<Ticket> obtenerPorEstado(EstadoTicket estado);

    Ticket asignarTecnico(Ticket ticket);

    Ticket cambiarEstado(Ticket ticket);

    Ticket cambiarPrioridad(Ticket ticket);

    Ticket resolver(Ticket ticket);

    Ticket actualizar(Ticket ticket);

    void eliminarPorId(Integer id);

}