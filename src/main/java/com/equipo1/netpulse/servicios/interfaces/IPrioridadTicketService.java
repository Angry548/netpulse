package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.PrioridadTicket;

import java.util.List;

public interface IPrioridadTicketService {

    PrioridadTicket crear(PrioridadTicket prioridad);

    PrioridadTicket buscarPorId(Integer idPrioridad);

    PrioridadTicket buscarPorNombre(String nombre);

    List<PrioridadTicket> obtenerTodos();

    PrioridadTicket actualizar(PrioridadTicket prioridad);

    void eliminarPorId(Integer idPrioridad);
}

