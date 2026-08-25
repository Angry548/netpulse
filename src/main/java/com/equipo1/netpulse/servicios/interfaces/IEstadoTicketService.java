package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.EstadoTicket;

import java.util.List;

public interface IEstadoTicketService {

    EstadoTicket crear(EstadoTicket estado);

    EstadoTicket buscarPorId(Integer id);

    EstadoTicket buscarPorNombre(String nombre);

    List<EstadoTicket> obtenerTodos();

    EstadoTicket actualizar(EstadoTicket estado);

    void eliminarPorId(Integer id);
}
